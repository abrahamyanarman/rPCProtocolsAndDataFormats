docker exec -it [kafka-container-id] kafka-topics.sh --create --topic avro-test --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1