package con.ss.jcrm.client.web.resource;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactPhoneNumberResource {

    private String countryCode;
    private String regionCode;
    private String phoneNumber;
    private String type;
}
