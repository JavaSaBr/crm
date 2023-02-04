package con.ss.jcrm.client.web.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ss.jcrm.client.api.ClientEmail;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientEmailResource(
    @Nullable String email,
    @Nullable String type
) {

    public static @NotNull ClientEmailResource from(@NotNull ClientEmail email) {
        return new ClientEmailResource(email.getEmail(), email.getType().description());
    }
}
