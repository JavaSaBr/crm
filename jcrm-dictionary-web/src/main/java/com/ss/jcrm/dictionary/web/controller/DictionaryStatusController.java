package com.ss.jcrm.dictionary.web.controller;

import static org.springframework.http.ResponseEntity.ok;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DictionaryStatusController {

    @GetMapping("/dictionary/status")
    @NotNull ResponseEntity<?> status() {
        return ok().build();
    }
}
