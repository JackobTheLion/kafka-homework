### Задание 1
1. Создадим топик
```bash
kafka-topics.bat --bootstrap-server localhost:9094 --topic balanced_topic --create --partitions 8 --replication-factor 3
```
Вывод
```log
WARNING: Due to limitations in metric names, topics with a period ('.') or underscore ('_') could collide. To avoid issues it is best to use either, but not both.
Created topic balanced_topic.
```
2. Посмотрим текущее положение партиций
```bash
kafka-topics.bat --bootstrap-server localhost:9094 --describe --topic balanced_topic
```
Вывод
```log
Topic: balanced_topic   TopicId: VROT-CGdTnWrgzeIYKn3lA PartitionCount: 8       ReplicationFactor: 3    Configs: 
        Topic: balanced_topic   Partition: 0    Leader: 2       Replicas: 2,0,1 Isr: 2,0,1      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 1    Leader: 0       Replicas: 0,1,2 Isr: 0,1,2      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 2    Leader: 1       Replicas: 1,2,0 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 3    Leader: 2       Replicas: 2,0,1 Isr: 2,0,1      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 4    Leader: 0       Replicas: 0,1,2 Isr: 0,1,2      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 5    Leader: 1       Replicas: 1,2,0 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 6    Leader: 0       Replicas: 0,2,1 Isr: 0,2,1      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 7    Leader: 2       Replicas: 2,1,0 Isr: 2,1,0      Elr: N/A        LastKnownElr: N/A
```
Т.к. фактор репликации 3, то каждая партиция реплицируется на каждом брокере. Лидеры же партиций распределены равномерно:  
Брокер 0 партиция 1, 4, 6  
Брокер 1 партиция 2, 5  
Брокер 2 партиция 0, 3, 7

3. Перераспределим партиции:
```bash
kafka-reassign-partitions.bat --bootstrap-server localhost:9094 --broker-list "1,2,3" --topics-to-move-json-file "./reassignment.json" --generate
```
Вывод
```log
Current partition replica assignment
{"version":1,"partitions":[]}

Proposed partition reassignment configuration
{"version":1,"partitions":[]}
```
```bash
kafka-reassign-partitions.bat --bootstrap-server localhost:9094 --reassignment-json-file ./reassignment.json --execute | jq > reassignment-result.json
```

4. Сымитируем отключение брокера 0 и посмотрим перераспределение партиций
```bash
kafka-topics.bat --bootstrap-server localhost:9095 --describe --topic balanced_topic
```
Вывод
```log
Topic: balanced_topic   TopicId: VROT-CGdTnWrgzeIYKn3lA PartitionCount: 8       ReplicationFactor: 3    Configs: 
        Topic: balanced_topic   Partition: 0    Leader: 1       Replicas: 0,1,2 Isr: 2,1        Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 1    Leader: 1       Replicas: 1,2,0 Isr: 1,2        Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 2    Leader: 2       Replicas: 2,0,1 Isr: 1,2        Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 3    Leader: 2       Replicas: 2,0,1 Isr: 2,1        Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 4    Leader: 2       Replicas: 0,2,1 Isr: 1,2        Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 5    Leader: 1       Replicas: 1,2,0 Isr: 1,2        Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 6    Leader: 2       Replicas: 2,0,1 Isr: 2,1        Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 7    Leader: 2       Replicas: 2,1,0 Isr: 2,1        Elr: N/A        LastKnownElr: N/A\
```
Как видим распределение лидеров изменилось. Также изменилось количество синхронизированных реплик.  
Брокер 1 партиция 0, 1, 5  
Брокер 2 партиция 2, 3, 4, 6, 7

5. Включим брокер и посмотрим описание топика
```bash
kafka-topics.bat --bootstrap-server localhost:9095 --describe --topic balanced_topic
```
Вывод
```log
Topic: balanced_topic   TopicId: VROT-CGdTnWrgzeIYKn3lA PartitionCount: 8       ReplicationFactor: 3    Configs: 
        Topic: balanced_topic   Partition: 0    Leader: 1       Replicas: 0,1,2 Isr: 2,1,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 1    Leader: 1       Replicas: 1,2,0 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 2    Leader: 2       Replicas: 2,0,1 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 3    Leader: 2       Replicas: 2,0,1 Isr: 2,1,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 4    Leader: 2       Replicas: 0,2,1 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 5    Leader: 1       Replicas: 1,2,0 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 6    Leader: 2       Replicas: 2,0,1 Isr: 2,1,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 7    Leader: 2       Replicas: 2,1,0 Isr: 2,1,0      Elr: N/A        LastKnownElr: N/A
```
Синхронизация восстановилась, однако лидеры остались без изменений. Подождем и повторим.
```log
Topic: balanced_topic   TopicId: VROT-CGdTnWrgzeIYKn3lA PartitionCount: 8       ReplicationFactor: 3    Configs: 
        Topic: balanced_topic   Partition: 0    Leader: 0       Replicas: 0,1,2 Isr: 2,1,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 1    Leader: 1       Replicas: 1,2,0 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 2    Leader: 2       Replicas: 2,0,1 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 3    Leader: 2       Replicas: 2,0,1 Isr: 2,1,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 4    Leader: 0       Replicas: 0,2,1 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 5    Leader: 1       Replicas: 1,2,0 Isr: 1,2,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 6    Leader: 2       Replicas: 2,0,1 Isr: 2,1,0      Elr: N/A        LastKnownElr: N/A
        Topic: balanced_topic   Partition: 7    Leader: 2       Replicas: 2,1,0 Isr: 2,1,0      Elr: N/A        LastKnownElr: N/A
```
Лидеры перебалансировались.  
Брокер 0 партиция 0, 4  
Брокер 1 партиция 1, 5  
Брокер 2 партиция 2, 3, 6, 7

### Задание 2
1. Собрать проект
```bash
mvn clean package
```
2. Сгенерировать сертификаты скриптом в папке kafka-certs
```bash
bash cert-creation
```
3. Запустить проект
```bash
docker-compose up
```