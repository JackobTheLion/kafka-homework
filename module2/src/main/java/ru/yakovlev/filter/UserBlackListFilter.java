package ru.yakovlev.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import ru.yakovlev.dto.Message;
import ru.yakovlev.dto.UserBlockAction;
import ru.yakovlev.serdes.HashSetSerde;
import ru.yakovlev.serdes.UserBlockActionSerde;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Slf4j
class UserBlackListFilter extends AbstractFilter {

    private static final String BLACKLIST_TOPIC_NAME = "blacklist.topic";
    private static final String BLACKLIST_AGGREGATE_TOPIC_NAME = "blacklist.aggregate.topic";

    private final String blacklistTopic;
    private final String blacklistAggregateTopic;
    private final GlobalKTable<Long, Set<Long>> userBlacklist;

    UserBlackListFilter(StreamsBuilder builder, Properties properties) {
        super(builder, properties);
        this.blacklistTopic = properties.getProperty(BLACKLIST_TOPIC_NAME);
        this.blacklistAggregateTopic = properties.getProperty(BLACKLIST_AGGREGATE_TOPIC_NAME);
        this.userBlacklist = getUserBlacklist(builder);
    }

    @Override
    KStream<Void, Message> applyFilter(KStream<Void, Message> messages) {
        return filterByBlackList(messages, userBlacklist);
    }

    private KStream<Void, Message> filterByBlackList(KStream<Void, Message> messageKStream, GlobalKTable<Long, Set<Long>> userBlacklist) {
        return messageKStream.leftJoin(
                        userBlacklist,
                        (key, value) -> value.getRecipientId(),
                        this::checkMessage
                )
                .filter((key, value) -> value != null)
                .selectKey((key, value) -> null);
        //Ключ намеренно устанавливается в null, т.к. активность пользователей может быть неравномерна
    }

    private Message checkMessage(Message message, Set<Long> blocked) {
        if (blocked != null && blocked.contains(message.getUserId())) {
            log.info("Message from user {} to {} blocked", message.getUserId(), message.getRecipientId());
            return null;
        } else return message;
    }

    private GlobalKTable<Long, Set<Long>> getUserBlacklist(StreamsBuilder builder) {
        builder.stream(blacklistTopic, Consumed.with(Serdes.Void(), new UserBlockActionSerde()))
                .peek(this::logAction)
                .groupBy((key, value) -> value.userId(), Grouped.with(Serdes.Long(), new UserBlockActionSerde()))
                .aggregate((Initializer<Set<Long>>) HashSet::new, (key, action, blackList) -> aggregate(action, blackList))
                .toStream()
                .to(blacklistAggregateTopic, Produced.with(Serdes.Long(), new HashSetSerde()));

        return builder.globalTable(
                blacklistAggregateTopic,
                Consumed.with(Serdes.Long(), new HashSetSerde()),
                Materialized.with(Serdes.Long(), new HashSetSerde())
        );
    }

    private Set<Long> aggregate(UserBlockAction action, Set<Long> blackList) {
        if (action.isBlocked()) blackList.add(action.blockedUser());
        else blackList.remove(action.blockedUser());
        return blackList;
    }

    private void logAction(Void key, UserBlockAction value) {
        log.info(value.isBlocked() ? "User {} blocked user {}" : "User {} unblocked user {}", value.userId(), value.blockedUser());
    }
}
