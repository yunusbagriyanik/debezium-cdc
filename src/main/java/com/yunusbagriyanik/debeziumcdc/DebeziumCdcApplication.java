package com.yunusbagriyanik.debeziumcdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class DebeziumCdcApplication {

    public static void main(String[] args) {
        SpringApplication.run(DebeziumCdcApplication.class, args);
    }

}
