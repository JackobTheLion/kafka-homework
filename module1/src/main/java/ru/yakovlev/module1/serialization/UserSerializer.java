package ru.yakovlev.module1.serialization;

import org.apache.kafka.common.serialization.Serializer;
import ru.yakovlev.module1.dto.User;

import java.nio.ByteBuffer;

@SuppressWarnings("unused")
public class UserSerializer implements Serializer<User> {
    @Override
    public byte[] serialize(String topic, User user) {
        byte[] nameBytes = user.name().getBytes();
        int nameSize = nameBytes.length;
        ByteBuffer buffer = ByteBuffer.allocate(4 + nameSize + 4);
        buffer.putInt(nameSize);
        buffer.put(nameBytes);
        buffer.putInt(user.id());
        return buffer.array();
    }
}
