package ru.yakovlev.module1;

import ru.yakovlev.module1.consumer.PullConsumer;
import ru.yakovlev.module1.consumer.PushConsumer;
import ru.yakovlev.module1.dto.UserGenerator;
import ru.yakovlev.module1.producer.Producer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final String TOPIC = "user_topic";

    public static void main(String[] args) {
        PullConsumer pullConsumer = new PullConsumer(List.of(TOPIC));
        PushConsumer pushConsumer = new PushConsumer(List.of(TOPIC));
        Producer producer = new Producer(new UserGenerator(getNames()), TOPIC);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(pullConsumer);
        executorService.submit(pushConsumer);
        executorService.submit(producer);
    }

    private static List<String> getNames() {
        return List.of("Ivan", "Andrey", "Oleg", "Egor", "Petr", "Olga", "Elena", "Julia", "Ekaterina", "Anastasia");
    }

}
