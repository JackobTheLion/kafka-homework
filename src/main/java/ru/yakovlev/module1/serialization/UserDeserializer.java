package ru.yakovlev.module1.serialization;

import org.apache.kafka.common.serialization.Deserializer;
import ru.yakovlev.module1.dto.User;

import java.nio.ByteBuffer;

@SuppressWarnings("unused")
public class UserDeserializer implements Deserializer<User> {
    @Override
    public User deserialize(String topic, byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int nameSize = buffer.getInt();
        byte[] nameBytes = new byte[nameSize];
        buffer.get(nameBytes);
        String name = new String(nameBytes);
        int id = buffer.getInt();
        return new User(id, name);
    }
}
