package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.*;
import java.util.List;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(of = "id")
public class DefaultSimpleClient implements SimpleClient {

    private final long id;

    private long assignerId;
    private long[] curatorIds;
    private long organizationId;

    private String firstName;
    private String secondName;
    private String thirdName;
    private String company;

    private LocalDate birthday;
    private Instant created;
    private Instant modified;

    private List<ClientPhoneNumber> phoneNumbers;
    private List<ClientEmail> emails;
    private List<ClientSite> sites;
    private List<ClientMessenger> messengers;

    private volatile int version;

    public DefaultSimpleClient(long id) {
        this.id = id;
        this.phoneNumbers = List.of();
        this.emails = List.of();
        this.sites = List.of();
        this.messengers = List.of();
    }
}
