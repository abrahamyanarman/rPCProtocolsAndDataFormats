package am.train.rpc.protocols.utils;

import org.apache.avro.Schema;

import java.io.IOException;
import java.io.InputStream;

public class SchemaLoader {
    public static Schema loadSchema(String path) {
        try (InputStream inputStream = SchemaLoader.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + path);
            }
            return new Schema.Parser().parse(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Avro schema from path: " + path, e);
        }
    }
}
