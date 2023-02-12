package com.ss.jcrm.registration.web.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import crm.contact.api.resource.MessengerResource;
import crm.contact.api.resource.PhoneNumberResource;
import crm.user.api.MinimalUser;
import crm.user.api.User;
import crm.base.web.resources.RestResource;
import com.ss.rlib.common.util.DateUtils;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonInclude(Include.NON_NULL)
public record MinimalUserOutResource(
    @NotNull String email,
    @Nullable String firstName,
    @Nullable String secondName,
    @Nullable String thirdName,
    @Nullable String birthday,
    @Nullable List<PhoneNumberResource> phoneNumbers,
    @Nullable List<MessengerResource> messengers,
    long id) implements RestResource {

  public static @NotNull MinimalUserOutResource from(@NotNull MinimalUser user) {
    return new MinimalUserOutResource(user.email(), null, null, null, null, null, null, user.id());
  }

  public static @NotNull MinimalUserOutResource from(@NotNull User user) {
    return new MinimalUserOutResource(user.email(),
        user.firstName(),
        user.secondName(),
        user.thirdName(),
        DateUtils.toString(user.birthday()),
        PhoneNumberResource.from(user.phoneNumbers()),
        MessengerResource.from(user.messengers()),
        user.id());
  }
}
