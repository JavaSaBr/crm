package com.ss.jcrm.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

@RestController
@AllArgsConstructor
public abstract class AsyncRestController {

    protected final Executor controllerExecutor;
}
