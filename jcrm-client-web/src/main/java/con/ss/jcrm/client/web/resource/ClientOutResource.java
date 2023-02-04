package con.ss.jcrm.client.web.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ss.jcrm.client.api.SimpleClient;
import com.ss.jcrm.web.resources.RestResource;
import com.ss.rlib.common.util.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientOutResource(
    long id,
    long assigner,
    long created,
    long modified,
    long[] curators,
    int version,
    @Nullable String firstName,
    @Nullable String secondName,
    @Nullable String thirdName,
    @Nullable String company,
    @Nullable String birthday,
    ClientPhoneNumberResource @Nullable [] phoneNumbers,
    ClientEmailResource @Nullable [] emails,
    ClientSiteResource @Nullable [] sites,
    ClientMessengerResource @Nullable [] messengers
) implements RestResource {

    public static @NotNull ClientOutResource from(@NotNull SimpleClient client) {
        return new ClientOutResource(
            client.id(),
            client.assignerId(),
            client.created().toEpochMilli(),
            client.modified().toEpochMilli(),
            client.curatorIds(),
            client.version(),
            client.firstName(),
            client.secondName(),
            client.thirdName(),
            client.company(),
            DateUtils.toString(client.birthday()),
            client.phoneNumbers().stream().map(ClientPhoneNumberResource::from).toArray(ClientPhoneNumberResource[]::new),
            client.emails().stream().map(ClientEmailResource::from).toArray(ClientEmailResource[]::new),
            client.sites().stream().map(ClientSiteResource::from).toArray(ClientSiteResource[]::new),
            client.messengers().stream().map(ClientMessengerResource::from).toArray(ClientMessengerResource[]::new)
        );
    }
}
