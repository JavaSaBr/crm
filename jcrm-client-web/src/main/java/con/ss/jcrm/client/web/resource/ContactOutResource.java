package con.ss.jcrm.client.web.resource;

import com.ss.jcrm.client.api.SimpleContact;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.DateUtils;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class ContactOutResource {

    private final long id;

    private final long assigner;
    private final long created;
    private final long modified;
    private final long[] curators;

    private final String firstName;
    private final String secondName;
    private final String thirdName;
    private final String company;
    private final String birthday;

    private final ContactPhoneNumberResource[] phoneNumbers;
    private final ContactEmailResource[] emails;
    private final ContactSiteResource[] sites;
    private final ContactMessengerResource[] messengers;

    public ContactOutResource(@NotNull SimpleContact contact) {
        this.id = contact.getId();
        this.assigner = contact.getAssignerId();
        this.curators = contact.getCuratorIds();
        this.firstName = contact.getFirstName();
        this.secondName = contact.getSecondName();
        this.thirdName = contact.getThirdName();
        this.company = contact.getCompany();
        this.created = contact.getCreated().toEpochMilli();
        this.modified = contact.getModified().toEpochMilli();
        this.birthday = DateUtils.toString(contact.getBirthday());
        this.phoneNumbers = ArrayUtils.mapNullable(
            contact.getPhoneNumbers(),
            ContactPhoneNumberResource::new,
            ContactPhoneNumberResource.class
        );
        this.emails = ArrayUtils.mapNullable(
            contact.getEmails(),
            ContactEmailResource::new,
            ContactEmailResource.class
        );
        this.sites = ArrayUtils.mapNullable(
            contact.getSites(),
            ContactSiteResource::new,
            ContactSiteResource.class
        );
        this.messengers = ArrayUtils.mapNullable(
            contact.getMessengers(),
            ContactMessengerResource::new,
            ContactMessengerResource.class
        );
    }
}
