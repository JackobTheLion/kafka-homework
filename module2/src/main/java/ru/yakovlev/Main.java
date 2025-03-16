package ru.yakovlev;

import lombok.extern.slf4j.Slf4j;
import ru.yakovlev.config.ConfigLoader;
import ru.yakovlev.filter.MessageFilterService;

import java.util.Properties;

@Slf4j
public class Main {

    public static void main(String[] args) {
        Properties properties = new ConfigLoader().loadProperties();
        MessageFilterService messageFilterService = new MessageFilterService(properties);
        messageFilterService.start();
    }
}