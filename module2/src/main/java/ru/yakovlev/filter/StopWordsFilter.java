package ru.yakovlev.filter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import ru.yakovlev.dto.Message;
import ru.yakovlev.dto.StopWord;
import ru.yakovlev.serdes.StopWordSerde;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
class StopWordsFilter extends AbstractFilter {

    private static final String STOP_WORDS_TOPIC_NAME = "stop.words.topic";

    private final Map<String, Pattern> stopWords;

    public StopWordsFilter(StreamsBuilder builder, Properties properties) {
        super(builder, properties);
        this.stopWords = new ConcurrentHashMap<>();
    }

    @Override
    KStream<Void, Message> applyFilter(KStream<Void, Message> messages) {
        buildUpdateStopWordsStream(properties.getProperty(STOP_WORDS_TOPIC_NAME));

        return messages.peek((key, message) -> {
            String messageText = message.getMessage();
            for (Map.Entry<String, Pattern> stopWord : stopWords.entrySet()) {
                if (messageText.contains(stopWord.getKey())) {
                    Matcher matcher = stopWord.getValue().matcher(messageText);
                    message.setMessage(matcher.replaceAll("***"));
                }
            }
        });
    }

    private void buildUpdateStopWordsStream(String topic) {
        builder.stream(topic, Consumed.with(Serdes.Void(), new StopWordSerde()))
                .foreach((key, stopWord) -> updateStopWords(stopWord));
    }

    private void updateStopWords(StopWord stopWord) {
        if (stopWord == null) return;
        String word = stopWord.word();
        if (stopWord.add()) {
            log.info("Adding word '{}' to stop list", word);
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE);
            stopWords.put(word, pattern);
        } else {
            log.info("Removing word '{}' from stop list", word);
            stopWords.remove(word);
        }
    }
}
