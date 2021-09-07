package con.ss.jcrm.client.web.resource;

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
    ClientPhoneNumberResource @Nullable [] phoneNumbers,
    ClientEmailResource @Nullable [] emails,
    ClientSiteResource @Nullable [] sites,
    ClientMessengerResource @Nullable [] messengers
) {

    public static @NotNull ClientInResource empty() {

        return new ClientInResource(
            0,
            null,
            0,
            0,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
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
        @Nullable String birthday
    ) {

        return new ClientInResource(
            0,
            null,
            0,
            0,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }
}
