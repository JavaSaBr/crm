package com.ss.jcrm.registration.web.controller;

import com.ss.jcrm.registration.web.validator.ResourceValidator;
import com.ss.jcrm.user.api.dao.UserDao;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserDao userDao;
    private final ResourceValidator resourceValidator;

    @GetMapping("/registration/user/exist/name/{name}")
    @NotNull CompletableFuture<ResponseEntity<?>> exist(@NotNull @PathVariable("name") String name) {
        resourceValidator.validateUserName(name);
        return userDao.existByNameAsync(name)
            .thenApply(exist -> new ResponseEntity<>(exist ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }
}
