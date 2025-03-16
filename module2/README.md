# kafka-homework
### Для запуска кластера Kafka:
1. Запустить Docker
2. Из корневой директории проекта выполнить
```shell
docker-compose -f docker-compose-KRaft.yml up -d
```
3. Для создания топиков (Kafka должна быть установлена локально).
```shell
kafka-topics.bat --create --topic raw-messages --partitions 3 --replication-factor 3 --bootstrap-server localhost:9095
kafka-topics.bat --create --topic user-blacklist --partitions 3 --replication-factor 3 --bootstrap-server localhost:9095
kafka-topics.bat --create --topic user-blacklist-aggregate --partitions 3 --replication-factor 3 --bootstrap-server localhost:9095
kafka-topics.bat --create --topic stop-words --partitions 3 --replication-factor 3 --bootstrap-server localhost:9095
kafka-topics.bat --create --topic filtered-messages --partitions 3 --replication-factor 3 --bootstrap-server localhost:9095
```

### Сборка и запуск приложения
1. Для сборки
```shell
mvn clean package
```
2. Запуск
```shell
java -jar -Dprofile=test module2/target/module2-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

### Отправка сообщений
1. Для отправки сообщений можно использовать console-producer
```shell
kafka-console-producer.bat --bootstrap-server localhost:9092 --topic TOPIC_NAME
```
## Заготовки сообщений:
Пример Kafka сообщения добавления в черный список:
{"userId": 1,"blockedUser": 4,"isBlocked": true}

Пример Kafka сообщения с текстом сообщения:
{"userId": 4,"recipientId": 1,"message": "Random message 24","timestamp": 1735148447916}

Пример Kafka сообщения с добавлением слова в черный список:
{"word":"message","add":true}

### Замечания по реализации
1. Предполагается, что все сообщения (за исключением сообщений в агрегированный топик черного списка) отправляются без ключей. 
Это сделано в целях обеспечения равномерного распределения сообщений между партициями.
2. В данной реализации черный список не сохраняется между запусками т.к. в целях практики был выбран способ чтения топика как стрима
