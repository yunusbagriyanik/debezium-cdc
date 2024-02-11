package com.yunusbagriyanik.debeziumcdc.handler;

import org.apache.kafka.connect.source.SourceRecord;

public interface DataProcessorHandler {
    void processEvent(SourceRecord sourceRecord);
}
