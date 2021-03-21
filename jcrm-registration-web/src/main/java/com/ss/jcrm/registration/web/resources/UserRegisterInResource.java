package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.web.resources.RestResource;
import org.jetbrains.annotations.Nullable;

public record UserRegisterInResource(
    @Nullable String name,
    @Nullable String password,
    long @Nullable [] roles
) implements RestResource {
}
