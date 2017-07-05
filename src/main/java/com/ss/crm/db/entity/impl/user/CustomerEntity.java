package com.ss.crm.db.entity.impl.user;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * The customer.
 *
 * @author JavaSaBr
 */
@Entity
public class CustomerEntity extends UserEntity {

    /**
     * The customer's email.
     */
    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    /**
     * The customer's phone number.
     */
    @Column(name = "phone_number", length = 50, nullable = false, unique = true)
    private String phoneNumber;

    /**
     * The flag of reading the customer.
     */
    @Column(name = "ready", nullable = false)
    private boolean ready;

    /**
     * Gets email.
     *
     * @return the email
     */
    @NotNull
    public String getEmail() {
        return email;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    @NotNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(@NotNull final String email) {
        this.email = email;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(@NotNull final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Is ready boolean.
     *
     * @return the boolean
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Sets ready.
     *
     * @param ready the ready
     */
    public void setReady(final boolean ready) {
        this.ready = ready;
    }
}
