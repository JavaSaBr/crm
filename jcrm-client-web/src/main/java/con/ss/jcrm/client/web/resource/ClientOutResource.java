package con.ss.jcrm.client.web.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ss.jcrm.client.api.SimpleClient;
import com.ss.jcrm.web.resources.RestResource;
import com.ss.rlib.common.util.ArrayUtils;
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
            client.getAssignerId(),
            client.getCreated().toEpochMilli(),
            client.getModified().toEpochMilli(),
            client.getCuratorIds(),
            client.version(),
            client.getFirstName(),
            client.getSecondName(),
            client.getThirdName(),
            client.getCompany(),
            DateUtils.toString(client.getBirthday()),
            ArrayUtils.mapNullable(client.getPhoneNumbers(),
                ClientPhoneNumberResource::from,
                ClientPhoneNumberResource.class
            ),
            ArrayUtils.mapNullable(
                client.getEmails(),
                ClientEmailResource::from,
                ClientEmailResource.class
            ),
            ArrayUtils.mapNullable(
                client.getSites(),
                ClientSiteResource::from,
                ClientSiteResource.class
            ),
            ArrayUtils.mapNullable(
                client.getMessengers(),
                ClientMessengerResource::from,
                ClientMessengerResource.class
            )
        );
    }
}
