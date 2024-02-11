package com.yunusbagriyanik.debeziumcdc.handler;

import io.debezium.data.Envelope;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.apache.kafka.connect.data.Struct;

@Service
public class DataProcessorHandlerImpl implements DataProcessorHandler {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String topicName;

    public DataProcessorHandlerImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void processEvent(SourceRecord sourceRecord) {
        logger.info("Operation: {}", getOperation((Struct) sourceRecord.value()));
        logger.info(sourceRecord);
        if (Envelope.Operation.CREATE.code().equals(getOperation((Struct) sourceRecord.value())))
            kafkaTemplate.send(topicName, sourceRecord.toString());
    }

    public static String getOperation(Struct sourceRecordValue) {
        return sourceRecordValue.getString(Envelope.FieldName.OPERATION);
    }
}
