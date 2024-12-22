package ru.yakovlev.module1.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.yakovlev.module1.dto.User;
import ru.yakovlev.module1.serialization.UserDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public class PushConsumer extends AbstractConsumer {
    private static final String NAME = "Push consumer";

    public PushConsumer(List<String> topics) {
        this.duration = Duration.ofMillis(1);
        this.properties = getProperties();
        this.topics = topics;
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    @Override
    public void run() {
        System.out.printf("Starting %s...\n", NAME);
        try (KafkaConsumer<String, User> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(topics);
            while (true) {
                ConsumerRecords<String, User> records = consumer.poll(duration);
                for (ConsumerRecord<String, User> record : records) {
                    log.info(MESSAGE_TEMPLATE, record.key(), record.value().toString(), record.partition(), record.offset());
                }
            }
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094,localhost:9095,localhost:9096"); //Адреса брокеров кафка
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); //Десериализатор ключа
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserDeserializer.class.getName()); //Десериализатор сообщения
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "push_group"); //Консьюмер группа. У каждого консьюмера своя, чтобы вычитывать все собщения
        properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 10 * 1024 * 1024); //Минимальный объем в байтах для вычитки
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //Начать вычитку с самого раннего невычитанного офсета
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); //Включаем автокоммит
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "6000"); //Таймаут сессии
        return properties;
    }
}
