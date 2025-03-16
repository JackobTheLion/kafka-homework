package ru.yakovlev.serdes;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serdes;
import ru.yakovlev.dto.Message;

public class MessageSerde extends Serdes.WrapperSerde<Message> {

    public MessageSerde() {
        super(new GeneralSerializer<>(), new GeneralDeserializer<>(new TypeReference<>() {
        }));
    }
}

