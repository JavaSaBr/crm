package con.ss.jcrm.client.web.resource;

import com.ss.jcrm.client.api.ContactSite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactSiteResource {

    private String url;
    private String type;

    public ContactSiteResource(@NotNull ContactSite site) {
        this.url = site.getUrl();
        this.type = site.getType().name();
    }
}
