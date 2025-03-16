package ru.yakovlev.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import ru.yakovlev.dto.User;
import ru.yakovlev.dto.UserGenerator;

import java.util.Properties;

@Slf4j
public class YCProducer implements Runnable {

    private final Properties properties;
    private final String topic;
    private final int msgCount;
    private final UserGenerator userGenerator;
    private final ObjectMapper mapper;

    public YCProducer(Properties properties, String topic, UserGenerator userGenerator, int mcgCount) {
        this.properties = properties;
        this.topic = topic;
        this.userGenerator = userGenerator;
        this.msgCount = mcgCount;
        this.mapper = new ObjectMapper();
    }

    @SneakyThrows
    public void run() {
        try (Producer<Integer, String> producer = new KafkaProducer<>(properties)) {
            for (int i = 1; i <= msgCount; i++) {
                User user = userGenerator.nextUser();
                ProducerRecord<Integer, String> record = new ProducerRecord<>(topic, user.getId(), mapper.writeValueAsString(user));
                producer.send(record);
                producer.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
