package con.ss.jcrm.client.web.resource;

import com.ss.jcrm.client.api.SimpleContact;
import com.ss.rlib.common.util.array.Array;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class ContactsOutResource {

    private final ContactOutResource[] contacts;

    public ContactsOutResource(@NotNull Array<SimpleContact> contacts) {
        this.contacts = contacts.stream()
            .map(ContactOutResource::new)
            .toArray(ContactOutResource[]::new);

    }
}
