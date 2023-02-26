package crm.client.api.impl;

import crm.client.api.SimpleClient;
import crm.contact.api.Email;
import crm.contact.api.Messenger;
import crm.contact.api.PhoneNumber;
import crm.contact.api.Site;
import java.util.Set;
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

    private Set<PhoneNumber> phoneNumbers;
    private Set<Email> emails;
    private Set<Site> sites;
    private Set<Messenger> messengers;

    private volatile int version;

    public DefaultSimpleClient(long id) {
        this.id = id;
        this.phoneNumbers = Set.of();
        this.emails = Set.of();
        this.sites = Set.of();
        this.messengers = Set.of();
    }
}
