package con.ss.jcrm.client.web.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInResource {

    private long id;
    private long[] curators;
    private long assigner;

    private int version;

    private String firstName;
    private String secondName;
    private String thirdName;
    private String company;
    private String birthday;

    private ContactPhoneNumberResource[] phoneNumbers;
    private ContactEmailResource[] emails;
    private ContactSiteResource[] sites;
    private ContactMessengerResource[] messengers;
}
