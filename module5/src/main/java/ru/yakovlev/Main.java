package ru.yakovlev;

import lombok.SneakyThrows;
import ru.yakovlev.config.PropertiesReader;
import ru.yakovlev.consumer.YCConsumer;
import ru.yakovlev.dto.UserGenerator;
import ru.yakovlev.producer.YCProducer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final String TOPIC = "topic";

    @SneakyThrows
    public static void main(String[] args) {
        PropertiesReader propertiesReader = new PropertiesReader();
        String topicName = propertiesReader.getProperty(TOPIC);
        UserGenerator userGenerator = new UserGenerator(getNames());

        YCConsumer consumer = new YCConsumer(propertiesReader.getConsumerProperties(), topicName);
        YCProducer producer = new YCProducer(propertiesReader.getProducerProperties(), topicName, userGenerator, 50);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(consumer);
        executorService.submit(producer);
    }

    private static List<String> getNames() {
        return List.of("Ivan", "Andrey", "Oleg", "Egor", "Petr", "Olga", "Elena", "Julia", "Ekaterina", "Anastasia");
    }

    private static Map<String, String> getRegistryMap(PropertiesReader propertiesReader) {
        Map<String, String> map = new HashMap<>();
        map.put("ssl.truststore.location", propertiesReader.getProperty("common.ssl.truststore.location"));
        map.put("ssl.truststore.password", propertiesReader.getProperty("common.ssl.truststore.password"));
        return map;
    }

}