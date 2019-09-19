package con.ss.jcrm.client.web.resource;

import com.ss.jcrm.client.api.ContactPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactPhoneNumberResource {

    private String countryCode;
    private String regionCode;
    private String phoneNumber;
    private String type;

    public ContactPhoneNumberResource(@NotNull ContactPhoneNumber phoneNumber) {
        this.countryCode = phoneNumber.getCountryCode();
        this.regionCode = phoneNumber.getRegionCode();
        this.phoneNumber = phoneNumber.getPhoneNumber();
        this.type = phoneNumber.getType().name();
    }
}
