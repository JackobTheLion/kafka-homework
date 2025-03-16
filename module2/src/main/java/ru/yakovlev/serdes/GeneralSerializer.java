package ru.yakovlev.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;

public class GeneralSerializer<T> implements Serializer<T> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public byte[] serialize(String topic, T data) {
        if (data == null) return null;
        return mapper.writeValueAsBytes(data);
    }
}
