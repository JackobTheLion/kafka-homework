package ru.yakovlev;

import lombok.SneakyThrows;
import ru.yakovlev.config.PropertiesReader;
import ru.yakovlev.consumer.Consumer;
import ru.yakovlev.dto.UserGenerator;
import ru.yakovlev.producer.Producer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final String TOPIC = "topic";

    @SneakyThrows
    public static void main(String[] args) {
        PropertiesReader propertiesReader = new PropertiesReader();
        String topicName = propertiesReader.getProperty(TOPIC);

        Consumer consumer = new Consumer(List.of(topicName), propertiesReader.getConsumerProperties());
        Producer producer = new Producer(new UserGenerator(getNames()), topicName, propertiesReader.getProducerProperties());

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(consumer);
        executorService.submit(producer);
    }

    private static List<String> getNames() {
        return List.of("Ivan", "Andrey", "Oleg", "Egor", "Petr", "Olga", "Elena", "Julia", "Ekaterina", "Anastasia");
    }

}
