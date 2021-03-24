package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ClientMessenger;
import com.ss.jcrm.client.api.MessengerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultClientMessenger implements ClientMessenger {

    private String login;
    private MessengerType type;
}
