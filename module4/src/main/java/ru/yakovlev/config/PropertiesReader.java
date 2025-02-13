package ru.yakovlev.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class PropertiesReader {

    private static final String PATH = "/application.properties";
    private static final String COMMON_PREFIX = "common.";
    public static final String CONSUMER_PREFIX = "consumer.";
    public static final String PRODUCER_PREFIX = "producer.";

    public final Properties properties;

    public PropertiesReader() {
        this.properties = readProperties();
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public Properties getConsumerProperties() {
        Properties props = getProperties(CONSUMER_PREFIX);
        log.info("Consumer properties: ");
        logProps(props);
        return props;
    }

    public Properties getProducerProperties() {
        Properties props = getProperties(PRODUCER_PREFIX);
        log.info("Producer properties: ");
        logProps(props);
        return props;
    }

    private void logProps(Properties props) {
        props.entrySet().stream()
                .sorted((o1, o2) -> o1.getKey().toString().compareTo(o2.toString()))
                .forEach(entry -> log.info("\t{} = {}", entry.getKey(), entry.getValue()));
    }

    private Properties getProperties(String prefix) {
        return properties.entrySet().stream()
                .filter(entry -> filterByPrefix(entry, prefix))
                .collect(Collectors
                        .toMap(
                                entry -> removePrefix(entry, prefix),
                                Map.Entry::getValue,
                                (a, b) -> b,
                                Properties::new
                        ));
    }

    private String removePrefix(Map.Entry<Object, Object> entry, String prefix) {
        return ((String) entry.getKey()).replace(COMMON_PREFIX, "").replace(prefix, "");
    }

    private boolean filterByPrefix(Map.Entry<Object, Object> entry, String prefix) {
        String key = (String) entry.getKey();
        return key.startsWith(COMMON_PREFIX) || key.startsWith(prefix);
    }

    @SneakyThrows
    private Properties readProperties() {
        Properties properties = new Properties();
        properties.load(PropertiesReader.class.getResourceAsStream(PATH));
        return properties;
    }
}

