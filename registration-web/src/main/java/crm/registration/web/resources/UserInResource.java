package crm.registration.web.resources;

import crm.base.web.resources.RestResource;
import crm.contact.api.resource.MessengerResource;
import crm.contact.api.resource.PhoneNumberResource;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record UserInResource(
    @Nullable String email,
    @Nullable String firstName,
    @Nullable String secondName,
    @Nullable String thirdName,
    char @Nullable [] password,
    @Nullable List<PhoneNumberResource> phoneNumbers,
    @Nullable List<MessengerResource> messengers,
    int @Nullable [] roles,
    @Nullable String birthday,
    long id) implements RestResource {

  public static @NotNull UserInResource from(
      @Nullable String email,
      @Nullable String firstName,
      @Nullable String secondName,
      @Nullable String thirdName,
      char @Nullable [] password,
      @Nullable List<PhoneNumberResource> phoneNumbers,
      @Nullable List<MessengerResource> messengers,
      int @Nullable [] roles,
      @Nullable String birthday) {
    return new UserInResource(
        email,
        firstName,
        secondName,
        thirdName,
        password,
        phoneNumbers,
        messengers,
        roles,
        birthday,
        0);
  }
}
