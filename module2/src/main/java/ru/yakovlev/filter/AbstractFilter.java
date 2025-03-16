package ru.yakovlev.filter;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import ru.yakovlev.dto.Message;

import java.util.Properties;

abstract class AbstractFilter {
    protected final StreamsBuilder builder;
    protected final Properties properties;

    AbstractFilter(StreamsBuilder builder, Properties properties) {
        this.builder = builder;
        this.properties = properties;
    }

    abstract KStream<Void, Message> applyFilter(KStream<Void, Message> messages);
}
