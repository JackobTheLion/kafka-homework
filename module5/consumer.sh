#! /bin/bash

kafka-console-consumer.sh \
    --bootstrap-server rc1a-hle26o0d9368l3pt.mdb.yandexcloud.net:9091,rc1b-pp421aduloqh4jcg.mdb.yandexcloud.net:9091,rc1d-8igeb47sq8lqapoh.mdb.yandexcloud.net:9091 \
    --topic nifi-users \
    --property print.key=true \
    --property key.separator=":" \
    --consumer-property security.protocol=SASL_SSL \
    --consumer-property sasl.mechanism=SCRAM-SHA-512 \
    --consumer-property ssl.truststore.location=ssl \
    --consumer-property ssl.truststore.password=123456 \
    --consumer-property sasl.jaas.config="org.apache.kafka.common.security.scram.ScramLoginModule required username='consumer' password='12345678';"
