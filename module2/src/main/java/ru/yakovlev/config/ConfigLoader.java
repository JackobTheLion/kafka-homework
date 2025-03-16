package ru.yakovlev.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import ru.yakovlev.serdes.HashSetSerde;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class ConfigLoader {
    private static final String DEFAULT_PROFILE = "";
    private static final String CONFIG_FILE_PREFIX = "application";
    private static final String CONFIG_FILE_SUFFIX = ".properties";

    @SneakyThrows
    public Properties loadProperties() {
        String configFilePath = getConfigFilePath();
        Properties properties = new Properties();
        try (InputStream resourceAsStream = ConfigLoader.class.getClassLoader().getResourceAsStream(configFilePath);
             InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(resourceAsStream));
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            properties.load(bufferedReader);
        }
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.LongSerde.class.getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, HashSetSerde.class.getName());
        log.info("Loaded properties: {}{}", System.lineSeparator(), propsAsString(properties));
        return properties;
    }

    private String getConfigFilePath() {
        String profile = getProfile();
        if (profile.isEmpty()) return CONFIG_FILE_PREFIX + CONFIG_FILE_SUFFIX;
        else return CONFIG_FILE_PREFIX + "-" + profile + CONFIG_FILE_SUFFIX;
    }

    private String getProfile() {
        String profile = System.getProperty("profile", DEFAULT_PROFILE);
        log.info(profile.isEmpty() ? "Chosen default profile" : "Chosen profile {}", profile);
        return profile;
    }

    private String propsAsString(Properties properties) {
        return properties.entrySet().stream()
                .sorted(Comparator.comparing(o -> o.getKey().toString()))
                .map(entry -> entry.getKey() + " = " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }
}
