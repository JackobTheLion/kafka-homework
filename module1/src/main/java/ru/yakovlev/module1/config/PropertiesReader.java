package ru.yakovlev.module1.config;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesReader {

    public static final String PATH = "src/main/resources/app.properties";

    public final Properties properties;

    public PropertiesReader() {
        this.properties = readProperties();
    }

    @SneakyThrows
    public Properties readProperties() {
        Properties properties = new Properties();
        properties.load(new FileInputStream(PATH));
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (value != null && value.contains("${")) {
                String resolvedValue = resolveEnvironmentVariables(value);
                properties.setProperty(key, resolvedValue);
            }
        }
        return properties;
    }

    private String resolveEnvironmentVariables(String value) {
        if (value == null) return null;
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");
        Matcher matcher = pattern.matcher(value);
        StringBuilder resolvedValue = new StringBuilder();
        while (matcher.find()) {
            String varName = matcher.group(1); // Имя переменной
            String envValue = System.getenv(varName); // Значение переменной окружения
            System.out.println("Var: " + varName + " = " + Path.of(envValue));
            matcher.appendReplacement(resolvedValue, envValue != null ? envValue : matcher.group(0));
        }
        matcher.appendTail(resolvedValue);
        return resolvedValue.toString();
    }
}

