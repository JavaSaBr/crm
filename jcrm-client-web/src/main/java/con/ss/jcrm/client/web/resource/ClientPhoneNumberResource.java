package con.ss.jcrm.client.web.resource;

import com.ss.jcrm.client.api.ClientPhoneNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ClientPhoneNumberResource(
    @Nullable String countryCode,
    @Nullable String regionCode,
    @Nullable String phoneNumber,
    @Nullable String type
) {

    public static @NotNull ClientPhoneNumberResource from(@NotNull ClientPhoneNumber phoneNumber) {
        return new ClientPhoneNumberResource(
            phoneNumber.getCountryCode(),
            phoneNumber.getRegionCode(),
            phoneNumber.getPhoneNumber(),
            phoneNumber.getType().name()
        );
    }
}
