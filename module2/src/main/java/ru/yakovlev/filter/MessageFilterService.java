package ru.yakovlev.filter;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import ru.yakovlev.dto.Message;
import ru.yakovlev.serdes.MessageSerde;
import ru.yakovlev.serdes.UserBlockActionSerde;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MessageFilterService {

    private static final String RAW_MESSAGES_TOPIC_NAME = "raw.message.topic";
    private static final String TARGET_TOPIC_NAME = "target.topic";
    private static final String STORE_NAME = "blocked_users_store";

    private final Properties properties;
    private final List<AbstractFilter> filters;
    private final StreamsBuilder builder;

    public MessageFilterService(Properties properties) {
        this.properties = properties;
        this.builder = new StreamsBuilder();
        this.filters = getFilters(builder, properties);
    }

    @SuppressWarnings("resource")
    public void start() {
        KStream<Void, Message> messages = getMessageKStream(builder, (String) properties.get(RAW_MESSAGES_TOPIC_NAME));

        KeyValueBytesStoreSupplier blockedUsersStoreSupplier = Stores.persistentKeyValueStore(STORE_NAME);
        builder.addStateStore(Stores.keyValueStoreBuilder(blockedUsersStoreSupplier, Serdes.String(), new UserBlockActionSerde()));

        for (AbstractFilter filter : filters) {
            messages = filter.applyFilter(messages);
        }

        messages.peek(((key, value) -> System.out.println(value)))
                .to(properties.getProperty(TARGET_TOPIC_NAME), Produced.with(Serdes.Void(), new MessageSerde()));

        Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        kafkaStreams.start();
    }

    private KStream<Void, Message> getMessageKStream(StreamsBuilder builder, String rawMessageTopic) {
        return builder.stream(rawMessageTopic, Consumed.with(Serdes.Void(), new MessageSerde()));
    }

    private List<AbstractFilter> getFilters(StreamsBuilder builder, Properties properties) {
        List<AbstractFilter> result = new ArrayList<>();
        result.add(new UserBlackListFilter(builder, properties));
        result.add(new StopWordsFilter(builder, properties));
        return result;
    }
}
