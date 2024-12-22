package ru.yakovlev.module1.consumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

abstract class AbstractConsumer implements Runnable {

    protected static final String MESSAGE_TEMPLATE = "Получено сообщение: key = {}, value = {}, partition = {}, offset = {}";

    protected Duration duration;
    protected Properties properties;
    protected List<String> topics;

}
