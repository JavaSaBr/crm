package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class DefaultSimpleContact implements SimpleContact {

    private final long id;

    private long assignerId;
    private long[] curatorIds;
    private long organizationId;

    private String firstName;
    private String secondName;
    private String thirdName;
    private String company;

    private LocalDate birthday;

    private ContactPhoneNumber[] phoneNumbers;
    private ContactEmail[] emails;
    private ContactSite[] sites;
    private ContactMessenger[] messengers;

    private volatile int version;

    public DefaultSimpleContact(long id) {
        this.id = id;
        this.phoneNumbers = EMPTY_PHONE_NUMBERS;
        this.emails = EMPTY_EMAILS;
        this.sites = EMPTY_SITES;
        this.messengers = EMPTY_MESSENGERS;
    }
}
