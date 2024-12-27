package ru.yakovlev.serdes;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serdes;
import ru.yakovlev.dto.UserBlockAction;

public class UserBlockActionSerde extends Serdes.WrapperSerde<UserBlockAction> {

    public UserBlockActionSerde() {
        super(new GeneralSerializer<>(), new GeneralDeserializer<>(new TypeReference<>() {
        }));
    }
}
