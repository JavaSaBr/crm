package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.web.resources.RestResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRegisterInResource implements RestResource {

    private String orgName;
    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String phoneNumber;

    private boolean subscribe;

    private long countryId;
}
