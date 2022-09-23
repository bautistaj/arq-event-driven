package com.persist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.persist.domain.LibraryEvent;
import com.persist.producer.LibraryEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LibraryEventController {
    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping("/v1/libraryEvent")
    public ResponseEntity<?> libraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {

        log.info("Before event");
        SendResult<Long, String> sendResult = this.libraryEventProducer.sendLibraryEventSynchronous(libraryEvent);
        log.info("After event {} ", sendResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
