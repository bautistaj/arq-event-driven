# ZooKeeper

## Start zookeeper server
```
cd kafka_2.12-3.2.1/bin

./zookeeper-server-start.sh ../config/zookeeper.properties 
```

## Start kafka server (BROKER)

Configure listeners
```
listeners=PLAINTEXT://:9092
auto.create.topics.enable=false
```

## Stark kafka broker
```
cd kafka_2.12-3.2.1/bin
./kafka-server-start.sh ../config/server.properties 

```

## Create a topic

```
./kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 4
```

## Instantiate Console PRODUCER Without Key

```
./kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic
```

## Instantiate Console PRODUCER With Key

```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning -property "key.separator= - " --property "print.key=true"
```
--from-beginning Help reading the messages in order

## Instantiate Console CONSUMER Without Key

```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning

```

## Instantiate Console CONSUMER With Key

```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning -property "key.separator= - " --property "print.key=true"

```

# Advanced Kafka CLI operations:

## List the topics in a cluster

```
./kafka-topics.sh --zookeeper localhost:2181 --list
```

## How to view consumer groups

```
./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list

```

## With Consumer Group

```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --group <group-name>

```

## Viewing the Commit Log

```
./kafka-run-class.sh kafka.tools.DumpLogSegments --deep-iteration --files /tmp/kafka-logs/test-topic-0/00000000000000000000.log
```


## Describe topic






