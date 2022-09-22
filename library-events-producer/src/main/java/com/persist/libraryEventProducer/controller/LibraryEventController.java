package com.persist.libraryEventProducer.controller;

import com.persist.libraryEventProducer.domain.LibraryEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("v1/library")
public class LibraryEventController {

    @PostMapping
    public ResponseEntity<?> libraryEvent(@RequestBody LibraryEvent libraryEvent) {
        //TODO invoke kafka producer
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
