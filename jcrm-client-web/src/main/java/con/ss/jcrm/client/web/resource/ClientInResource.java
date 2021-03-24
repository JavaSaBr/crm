package con.ss.jcrm.client.web.resource;

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
    ClientPhoneNumberResource @Nullable [] phoneNumbers,
    ClientEmailResource @Nullable [] emails,
    ClientSiteResource @Nullable [] sites,
    ClientMessengerResource @Nullable [] messengers
) {

}
