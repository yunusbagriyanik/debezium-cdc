package com.yunusbagriyanik.debeziumcdc.listener.debezium;

import com.yunusbagriyanik.debeziumcdc.handler.DataProcessorHandler;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class DebeziumSourceEventListener {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final Executor executor;
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
    private final DataProcessorHandler dataProcessorHandler;

    public DebeziumSourceEventListener(Configuration mongodbConnector, DataProcessorHandler dataProcessorHandler) {
        this.executor = Executors.newSingleThreadExecutor();

        this.debeziumEngine =
                DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                        .using(mongodbConnector.asProperties())
                        .notifying(this::handleChangeEvent)
                        .build();

        this.dataProcessorHandler = dataProcessorHandler;
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        try {
            SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
            dataProcessorHandler.processEvent(sourceRecord);
        } catch (Exception e) {
            logger.error("DebeziumSourceEventListener Error: {}", e.getMessage());
        }
    }

    @PostConstruct
    private void init() {
        try {
            this.executor.execute(debeziumEngine);
        } catch (Exception e) {
            logger.error("Error during initialization: {}", e.getMessage());
        }
    }

    @PreDestroy
    private void destroy() throws IOException {
        if (this.debeziumEngine != null)
            this.debeziumEngine.close();
    }
}
