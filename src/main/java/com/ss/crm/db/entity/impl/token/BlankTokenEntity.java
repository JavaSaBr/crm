package com.ss.crm.db.entity.impl.token;

import static java.time.ZonedDateTime.now;
import com.ss.crm.db.entity.impl.BaseEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * The type Blank token entity.
 *
 * @author JavaSaBr
 */
@Entity
@Table(name = "BLANK_TOKEN_ENTITY", indexes = {
        @Index(columnList = "token", name = "access_token_token_index"),
        @Index(columnList = "user_id", name = "access_token_user_id_index"),
        @Index(columnList = "expiry", name = "access_token_expiry_index")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue(value = "1")
public class BlankTokenEntity extends BaseEntity {

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

    /**
     * The flag of invaliding this token.
     */
    @Column(name = "invalid", nullable = false)
    private boolean invalid;

    /**
     * Gets token.
     *
     * @return the token
     */
    @NotNull
    public String getToken() {
        return token;
    }

    /**
     * Sets token.
     *
     * @param token the token
     */
    public void setToken(@NotNull final String token) {
        this.token = token;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(final long userId) {
        this.userId = userId;
    }

    /**
     * Gets expiry.
     *
     * @return the expiry
     */
    @NotNull
    public ZonedDateTime getExpiry() {
        return expiry;
    }

    /**
     * Sets expiry.
     *
     * @param expiry the expiry
     */
    public void setExpiry(@NotNull final ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    /**
     * Is invalid boolean.
     *
     * @return the boolean
     */
    public boolean isInvalid() {
        return invalid;
    }

    /**
     * Sets invalid.
     *
     * @param invalid the invalid
     */
    public void setInvalid(final boolean invalid) {
        this.invalid = invalid;
    }

    /**
     * Is expired boolean.
     *
     * @return true if this token is expired.
     */
    public boolean isExpired() {
        return now(expiry.getZone()).isAfter(expiry);
    }

    @Override
    public String toString() {
        return "BlankTokenEntity{" + "token='" + token + '\'' + ", userId=" + userId + ", expiry=" + expiry +
                ", invalid=" + invalid + '}';
    }
}
