package ru.yakovlev.producer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yakovlev.dto.User;
import ru.yakovlev.dto.UserGenerator;

import java.util.Properties;

@Slf4j
public class Producer implements Runnable {

    private final UserGenerator userGenerator;
    private final Properties properties;
    private final String topic;

    public Producer(UserGenerator userGenerator, String topic, Properties properties) {
        this.userGenerator = userGenerator;
        this.properties = properties;
        this.topic = topic;
    }

    @Override
    @SneakyThrows
    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
    public void run() {
        log.info("Starting producer...");
        try (KafkaProducer<String, User> producer = new KafkaProducer<>(properties)) {
            while (true) {
                User user = userGenerator.nextUser();
                ProducerRecord<String, User> record = new ProducerRecord<>(topic, user.name(), user);
                producer.send(record);
                Thread.sleep(100);
            }
        }
    }
}
