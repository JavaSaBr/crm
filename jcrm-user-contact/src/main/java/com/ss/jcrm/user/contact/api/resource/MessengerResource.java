package com.ss.jcrm.user.contact.api.resource;

import com.ss.jcrm.user.contact.api.Messenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessengerResource {

    private String login;
    private String type;

    public MessengerResource(@NotNull Messenger messenger) {
        this.login = messenger.getLogin();
        this.type = messenger.getType().name();
    }
}
