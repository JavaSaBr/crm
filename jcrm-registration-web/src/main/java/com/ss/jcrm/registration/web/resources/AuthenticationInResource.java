package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.web.resources.RestResource;
import org.jetbrains.annotations.Nullable;

public record AuthenticationInResource(
    @Nullable String login,
    @Nullable char[] password
) implements RestResource {}
