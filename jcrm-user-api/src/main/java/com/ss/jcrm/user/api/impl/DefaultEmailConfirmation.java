package com.ss.jcrm.user.api.impl;

import com.ss.jcrm.user.api.EmailConfirmation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "code", "email"})
public class DefaultEmailConfirmation implements EmailConfirmation {

    private long id;
    private String code;
    private String email;
    private Instant expiration;
}
