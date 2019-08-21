package com.ss.jcrm.task.api.impl;

import com.ss.jcrm.task.api.SimpleContact;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Setter
@ToString
@AllArgsConstructor
@Getter(onMethod_ = @NotNull)
@EqualsAndHashCode(of = "id")
public class DefaultSimpleContact implements SimpleContact {

    private final long id;

    private long organizationId;

    private String firstName;
    private String secondName;
    private String thirdName;

    private volatile int version;

    public DefaultSimpleContact(long id) {
        this.id = id;
    }
}
