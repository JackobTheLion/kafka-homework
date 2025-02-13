package ru.yakovlev.dto;

import java.util.List;
import java.util.Random;

public class UserGenerator {

    private final List<String> names;
    private final Random random;

    public UserGenerator(List<String> names) {
        this.names = names;
        this.random = new Random();
    }

    public User nextUser() {
        int id = random.nextInt();
        int i = random.nextInt(names.size());
        String name = names.get(i);
        return new User(id, name);
    }
}
