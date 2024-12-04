package ru.yakovlev.module1.producer;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.yakovlev.module1.dto.User;
import ru.yakovlev.module1.dto.UserGenerator;
import ru.yakovlev.module1.serialization.UserSerializer;

import java.util.Properties;

public class Producer implements Runnable {

    private final UserGenerator userGenerator;
    private final Properties properties;
    private final String topic;

    public Producer(UserGenerator userGenerator, String topic) {
        this.userGenerator = userGenerator;
        this.properties = getProperties();
        this.topic = topic;
    }

    @Override
    @SneakyThrows
    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
    public void run() {
        System.out.println("Starting producer...");
        try (KafkaProducer<String, User> producer = new KafkaProducer<>(properties)) {
            while (true) {
                User user = userGenerator.nextUser();
                ProducerRecord<String, User> record = new ProducerRecord<>(topic, user.name(), user);
                producer.send(record);
                Thread.sleep(100);
            }
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094,localhost:9095,localhost:9096"); //Бутстрапы кафки
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); //Сериалайзер ключа
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserSerializer.class.getName()); //Сериалайзер сообщения
        properties.put(ProducerConfig.RETRIES_CONFIG, 3); //количество ретраев при неуспешной отправке
        properties.put(ProducerConfig.ACKS_CONFIG, "all"); //количество реплик
        return properties;
    }
}
