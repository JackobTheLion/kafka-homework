package ru.yakovlev.serdes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashSet;
import java.util.Set;

public class HashSetSerde implements Serde<Set<Long>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Serializer<Set<Long>> serializer() {
        return (topic, data) -> {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize Set<Long>", e);
            }
        };
    }

    @Override
    public Deserializer<Set<Long>> deserializer() {
        return (topic, data) -> {
            if (data == null) return new HashSet<>();
            try {
                return objectMapper.readValue(data, new TypeReference<>() {
                });
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize Set<Long>", e);
            }
        };
    }
}
