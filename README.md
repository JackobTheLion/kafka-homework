# kafka-homework
### Для запуска кластера Kafka:
1. Запустить Docker
2. Из корневой директории проекта выполнить
```shell
docker-compose -f docker-compose-KRaft.yml up -d
```
3. Для создания топика (Kafka должна быть установлена локально)
```shell
KAFKA_FOLDER\bin\kafka-topics.sh --create --topic user_topic --partitions 3 --replication-factor 3 --bootstrap-server localhost:9095
```

### Сборка и запуск приложения
1. Для сборки
```shell
mvn clean package
```
2. Запуск
```shell
java -jar .\target\kafka-homework-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```
