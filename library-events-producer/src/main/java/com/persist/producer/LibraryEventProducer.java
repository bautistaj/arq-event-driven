package com.persist.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persist.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class LibraryEventProducer {
    @Autowired
    KafkaTemplate<Long, String> kafkaTemplate;
    @Autowired
    ObjectMapper mapper;
    private String topic = "library-events";

    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
        Long key = libraryEvent.getLibraryEventId();
        String value = mapper.writeValueAsString(libraryEvent);
        ListenableFuture<SendResult<Long, String>>  listenableFuture = kafkaTemplate.sendDefault(key, value);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Long, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    public SendResult<Long, String>  sendLibraryEventSynchronous(LibraryEvent libraryEvent) throws JsonProcessingException {
        Long key = libraryEvent.getLibraryEventId();
        String value = mapper.writeValueAsString(libraryEvent);

        try {

            SendResult<Long, String>  sendResult = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.SECONDS);
            return  sendResult;

        } catch (ExecutionException | InterruptedException exception) {
            exception.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            log.info("Process finish");
        }

        return null;
    }

    public void sendLibraryEventV2(LibraryEvent libraryEvent) throws JsonProcessingException {
        Long key = libraryEvent.getLibraryEventId();
        String value = mapper.writeValueAsString(libraryEvent);

        ProducerRecord<Long, String>  producerRecord = this.buildProducerRecord(key, value, this.topic);

        ListenableFuture<SendResult<Long, String>>  listenableFuture = kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Long, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    private ProducerRecord<Long, String> buildProducerRecord(Long key, String value, String topic) {
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<Long, String>(topic, null, key, value, recordHeaders);
    }

    private void handleSuccess(Long key, String value, SendResult<Long, String> result) {
        log.info("Message was send successFully, key: {} and value {}, partitions: {} ",
                key, value, result.getRecordMetadata().partition());
    }

    private void handleFailure(Long key, String value, Throwable ex) {
        log.error("Error sending message key: {} and value {}, message: {}", key, value, ex.getMessage());
        try {
            throw ex;
        }catch (Throwable throwable) {
            log.error("Error in OnFailure {}", throwable.getMessage());
        }
    }

}
