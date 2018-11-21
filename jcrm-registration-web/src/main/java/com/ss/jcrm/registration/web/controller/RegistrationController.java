package com.ss.jcrm.registration.web.controller;

import com.ss.jcrm.registration.web.resources.UserRegisterResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class RegistrationController {

    @PostMapping(
        path = "/register",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NotNull CompletableFuture<?> register(@NotNull UserRegisterResource resource) {
        return CompletableFuture.completedFuture(ResponseEntity.ok());
    }
}
