#!/bin/bash

KAFKA_BROKER="kafka-server-0:9092"
sleep 25  # Ждем, пока Kafka полностью запустится
kafka-topics --create --topic user_topic --partitions 3 --replication-factor 1 --bootstrap-server $KAFKA_BROKER
