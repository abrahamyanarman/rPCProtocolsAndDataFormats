package am.train.rpc.protocols.consumer;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class AvroKafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroKafkaConsumer.class);

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:19092");
        properties.setProperty("group.id", "test-group");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        properties.setProperty("schema.registry.url", "http://localhost:8081");

        try (Consumer<String, GenericRecord> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(Collections.singletonList("avro-test"));

            while (true) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, GenericRecord> record : records) {
                    LOGGER.info("Record: Offset = {}, Key = {}, Value = {}", record.offset(), record.key(), record.value());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error during pulling", e);
        }
    }
}
