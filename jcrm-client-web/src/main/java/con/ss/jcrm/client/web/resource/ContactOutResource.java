package con.ss.jcrm.client.web.resource;

import com.ss.jcrm.client.api.SimpleContact;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class ContactOutResource {

    private final String firstName;
    private final String secondName;
    private final String thirdName;

    public ContactOutResource(@NotNull SimpleContact contact) {
        this.firstName = contact.getFirstName();
        this.secondName = contact.getSecondName();
        this.thirdName = contact.getThirdName();
    }
}
