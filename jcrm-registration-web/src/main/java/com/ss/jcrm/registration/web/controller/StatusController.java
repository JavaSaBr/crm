package com.ss.jcrm.registration.web.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping(
        path = "/status",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull ResponseEntity<?> status() {
        return ResponseEntity.ok().build();
    }
}
