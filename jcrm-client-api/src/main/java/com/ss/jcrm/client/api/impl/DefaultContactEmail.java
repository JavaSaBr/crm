package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ContactEmail;
import com.ss.jcrm.client.api.EmailType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultContactEmail implements ContactEmail {

    private String email;
    private EmailType type;
}
