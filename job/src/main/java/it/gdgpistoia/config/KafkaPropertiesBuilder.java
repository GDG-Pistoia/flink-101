package it.gdgpistoia.config;

import java.util.Objects;
import java.util.Properties;

public class KafkaPropertiesBuilder {

    final Properties properties = new Properties();

    public KafkaPropertiesBuilder bootstrapServers(String value){
        Objects.requireNonNull(value);
        properties.setProperty("bootstrap.servers", value);
        return this;
    }

    public KafkaPropertiesBuilder groupId(String value){
        Objects.requireNonNull(value);
        properties.setProperty("group.id", value);
        return this;
    }

    public Properties build(){
        return properties;
    }

    public static KafkaPropertiesBuilder builder(){
        return new KafkaPropertiesBuilder();
    }
}
