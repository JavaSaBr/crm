package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ContactMessenger;
import com.ss.jcrm.client.api.MessengerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultContactMessenger implements ContactMessenger {

    private String login;
    private MessengerType type;
}
