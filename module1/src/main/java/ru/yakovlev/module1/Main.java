package ru.yakovlev.module1;

import lombok.SneakyThrows;
import ru.yakovlev.module1.config.PropertiesReader;
import ru.yakovlev.module1.consumer.PullConsumer;
import ru.yakovlev.module1.consumer.PushConsumer;
import ru.yakovlev.module1.dto.UserGenerator;
import ru.yakovlev.module1.producer.Producer;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final String TOPIC = "user_topic";

    @SneakyThrows
    public static void main(String[] args) {
        Properties properties = new PropertiesReader().readProperties();

        properties.forEach((key, value) -> System.out.println(key + " = " + value));


//        PullConsumer pullConsumer = new PullConsumer(List.of(TOPIC));
//        PushConsumer pushConsumer = new PushConsumer(List.of(TOPIC));
//        Producer producer = new Producer(new UserGenerator(getNames()), TOPIC);
//
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
//        executorService.submit(pullConsumer);
//        executorService.submit(pushConsumer);
//        executorService.submit(producer);
    }

    private static List<String> getNames() {
        return List.of("Ivan", "Andrey", "Oleg", "Egor", "Petr", "Olga", "Elena", "Julia", "Ekaterina", "Anastasia");
    }

}
