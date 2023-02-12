package con.ss.jcrm.client.web.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import crm.client.api.SimpleClient;
import crm.base.web.resources.RestResource;
import com.ss.rlib.common.util.DateUtils;
import crm.contact.api.resource.EmailResource;
import crm.contact.api.resource.MessengerResource;
import crm.contact.api.resource.PhoneNumberResource;
import crm.contact.api.resource.SiteResource;
import java.util.List;
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
    @Nullable List<PhoneNumberResource> phoneNumbers,
    @Nullable List<EmailResource> emails,
    @Nullable List<SiteResource> sites,
    @Nullable List<MessengerResource> messengers) implements RestResource {

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
        PhoneNumberResource.from(client.phoneNumbers()),
        EmailResource.from(client.emails()),
        SiteResource.from(client.sites()),
        MessengerResource.from(client.messengers()));
  }
}
