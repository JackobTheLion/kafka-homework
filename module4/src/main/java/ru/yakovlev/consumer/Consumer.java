package ru.yakovlev.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.yakovlev.dto.User;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public class Consumer implements Runnable {

    protected static final String MESSAGE_TEMPLATE = "Получено сообщение: key = {}, value = {}, partition = {}, offset = {}";

    protected Duration duration;
    protected Properties properties;
    protected List<String> topics;

    public Consumer(List<String> topics, Properties properties) {
        this.duration = Duration.ofMillis(10000);
        this.properties = properties;
        this.topics = topics;
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    @Override
    public void run() {
        log.info("Starting consumer...\n");
        try (KafkaConsumer<String, User> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(topics);
            while (true) {
                ConsumerRecords<String, User> records = consumer.poll(duration);
                for (ConsumerRecord<String, User> record : records) {
                    log.info(MESSAGE_TEMPLATE, record.key(), record.value().toString(), record.partition(), record.offset());
                }
                consumer.commitSync();
            }
        }
    }
}
