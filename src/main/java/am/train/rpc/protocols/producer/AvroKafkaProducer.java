package am.train.rpc.protocols.producer;

import am.train.rpc.protocols.utils.SchemaLoader;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class AvroKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroKafkaProducer.class);
    private static final String SCHEMA_PATH = "avro/message.avsc";

    public static void main(String[] args) {
        Schema schema = SchemaLoader.loadSchema(SCHEMA_PATH);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:19092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        properties.setProperty("schema.registry.url", "http://localhost:8081");

        try (Producer<String, GenericRecord> producer = new KafkaProducer<>(properties)) {
            GenericRecord record = new GenericData.Record(schema);
            record.put("content", "Hello Kafka!");
            record.put("timestamp", System.currentTimeMillis()); // add for version to see version update in schema registry
            LOGGER.info("Producing record, record: {}", record);

            producer.send(new ProducerRecord<>("avro-test", record));
        } catch (Exception e) {
            LOGGER.error("Error during producing record", e);
        }
    }
}
