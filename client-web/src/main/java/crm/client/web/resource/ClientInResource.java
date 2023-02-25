package crm.client.web.resource;

import crm.contact.api.resource.EmailResource;
import crm.contact.api.resource.MessengerResource;
import crm.contact.api.resource.PhoneNumberResource;
import crm.contact.api.resource.SiteResource;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ClientInResource(
    long id,
    long[] curators,
    long assigner,
    int version,
    @Nullable String firstName,
    @Nullable String secondName,
    @Nullable String thirdName,
    @Nullable String company,
    @Nullable String birthday,
    @Nullable List<PhoneNumberResource> phoneNumbers,
    @Nullable List<EmailResource> emails,
    @Nullable List<SiteResource> sites,
    @Nullable List<MessengerResource> messengers) {

  public static @NotNull ClientInResource empty() {
    return new ClientInResource(0, null, 0, 0, null, null, null, null, null, null, null, null, null);
  }

  public static @NotNull ClientInResource withoutContacts(
      long id,
      long[] curators,
      long assigner,
      int version,
      @Nullable String firstName,
      @Nullable String secondName,
      @Nullable String thirdName,
      @Nullable String company,
      @Nullable String birthday) {
    return new ClientInResource(id,
        curators,
        assigner,
        version,
        firstName,
        secondName,
        thirdName,
        company,
        birthday,
        null,
        null,
        null,
        null);
  }
}
