package com.yunusbagriyanik.debeziumcdc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebeziumConnectorConfig {
    @Value("${spring.data.mongodb.uri}")
    private String mongoConnStr;
    @Value("${spring.datasource.url}")
    private String postgresUrl;
    @Value("${spring.datasource.username}")
    private String postgresUsername;
    @Value("${spring.datasource.password}")
    private String postgresPassword;

    @Bean
    public io.debezium.config.Configuration mongodbConnector() {
        return io.debezium.config.Configuration.create()
                .with("name", "spring-debezium-cdc")
                .with("connector.class", "io.debezium.connector.mongodb.MongoDbConnector")
                .with("mongodb.connection.string", mongoConnStr)
                .with("topic.prefix", "spring-debezium-cdc")
                .with("database.whitelist", "test")
                .with("errors.log.include.messages", "true")
                .with("offset.storage", "io.debezium.storage.jdbc.offset.JdbcOffsetBackingStore")
                .with("offset.storage.jdbc.url", postgresUrl)
                .with("offset.storage.jdbc.user", postgresUsername)
                .with("offset.storage.jdbc.password", postgresPassword)
                .build();
    }
}
