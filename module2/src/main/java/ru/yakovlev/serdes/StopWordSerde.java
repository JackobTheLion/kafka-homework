package ru.yakovlev.serdes;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serdes;
import ru.yakovlev.dto.StopWord;

public class StopWordSerde extends Serdes.WrapperSerde<StopWord> {
    public StopWordSerde() {
        super(new GeneralSerializer<>(), new GeneralDeserializer<>(new TypeReference<>() {
        }));
    }
}
