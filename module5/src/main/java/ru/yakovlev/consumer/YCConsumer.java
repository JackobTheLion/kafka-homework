package ru.yakovlev.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class YCConsumer implements Runnable {

    private final Properties properties;
    private final String topic;

    public YCConsumer(Properties properties, String topic) {
        this.properties = properties;
        this.topic = topic;
    }

    @Override
    public void run() {
        try (Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(Collections.singletonList(topic));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Read from Kafka key: {}, value: {}", record.key(), record.value());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
