package con.ss.jcrm.client.web.resource;

import com.ss.jcrm.client.api.ContactMessenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessengerResource {

    private String login;
    private String type;

    public ContactMessengerResource(@NotNull ContactMessenger messenger) {
        this.login = messenger.getLogin();
        this.type = messenger.getType().name();
    }
}
