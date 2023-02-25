package crm.registration.web.resources;

import crm.base.web.resources.RestResource;
import org.jetbrains.annotations.Nullable;

public record AuthenticationInResource(@Nullable String login, char @Nullable [] password)
    implements RestResource {}
