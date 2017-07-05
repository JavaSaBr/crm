package com.ss.crm.db.entity.impl;

import static java.time.ZonedDateTime.now;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.ZonedDateTime;

/**
 * @author JavaSaBr
 */
@Entity
@Table(name = "ACCESS_TOKEN_ENTITY", indexes = {
        @Index(columnList = "token", name = "access_token_token_index"),
        @Index(columnList = "user_id", name = "access_token_user_id_index"),
        @Index(columnList = "expiry", name = "access_token_expiry_index")
})
public class AccessTokenEntity extends BaseEntity {

    /**
     * The token value.
     */
    @Column(name = "token", unique = true, updatable = false, nullable = false)
    private String token;

    /**
     * The user id.
     */
    @Column(name = "user_id", updatable = false, nullable = false)
    private long userId;

    /**
     * The expiry date.
     */
    @Column(name = "expiry", updatable = false, nullable = false)
    private ZonedDateTime expiry;

    public AccessTokenEntity() {
    }

    public AccessTokenEntity(@Nullable final Long id, @Nullable final String token, final long userId,
                             @Nullable final ZonedDateTime expiry) {
        super(id);
        this.token = token;
        this.userId = userId;
        this.expiry = expiry;
    }

    /**
     * @param userId the user id.
     */
    public void setUserId(final long userId) {
        this.userId = userId;
    }

    /**
     * @return the user id.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param token the token value.
     */
    public void setToken(@NotNull final String token) {
        this.token = token;
    }

    /**
     * @param expiry the expiry date.
     */
    public void setExpiry(@NotNull final ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    /**
     * Get a token value.
     *
     * @return the token value.
     */
    @NotNull
    public String getToken() {
        return token;
    }

    /**
     * @return true if this token is expired.
     */
    public boolean isExpired() {
        return now(expiry.getZone()).isAfter(expiry);
    }

    @Override
    public String toString() {
        return "AccessTokenEntity{" + "token='" + token + '\'' + ", userId=" + userId + ", expiry=" + expiry + '}';
    }
}
