package con.ss.jcrm.client.web.resource;

import com.ss.jcrm.client.api.ContactEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactEmailResource {

    private String email;
    private String type;

    public ContactEmailResource(@NotNull ContactEmail email) {
        this.email = email.getEmail();
        this.type = email.getType().name();
    }
}
