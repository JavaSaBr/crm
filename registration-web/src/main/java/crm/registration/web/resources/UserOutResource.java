package crm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import crm.base.util.WithId;
import crm.contact.api.resource.MessengerResource;
import crm.contact.api.resource.PhoneNumberResource;
import crm.user.api.User;
import crm.base.web.resources.RestResource;
import com.ss.rlib.common.util.DateUtils;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(Include.NON_NULL)
public record UserOutResource(
    @NotNull String email,
    @Nullable String firstName,
    @Nullable String secondName,
    @Nullable String thirdName,
    @Nullable String birthday,
    @Nullable List<PhoneNumberResource> phoneNumbers,
    @Nullable List<MessengerResource> messengers,
    long @NotNull [] roles,
    long id,
    long created,
    long modified) implements RestResource {

  public static @NotNull UserOutResource from(@NotNull User user) {
    return new UserOutResource(
        user.email(),
        user.firstName(),
        user.secondName(),
        user.thirdName(),
        DateUtils.toString(user.birthday()),
        PhoneNumberResource.from(user.phoneNumbers()),
        MessengerResource.from(user.messengers()),
        WithId.toIds(user.roles()),
        user.id(),
        user.created().toEpochMilli(),
        user.modified().toEpochMilli());
  }
}
