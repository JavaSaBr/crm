package com.ss.crm.endpoint.service.impl.user;

import org.jetbrains.annotations.Nullable;

/**
 * The request params of creating a customer.
 *
 * @author JavaSaBr
 */
public class RegisterCustomerInfo {

    /**
     * The customer's email.
     */
    @Nullable
    private String email;

    /**
     * The customer's name
     */
    @Nullable
    private String name;

    /**
     * The customer's phone number.
     */
    @Nullable
    private String phoneNumber;

    /**
     * Gets email.
     *
     * @return the email
     */
    @Nullable
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(@Nullable final String email) {
        this.email = email;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(@Nullable final String name) {
        this.name = name;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(@Nullable final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "RegisterCustomerInfo{" + "email='" + email + '\'' + ", name='" + name + '\'' + ", phoneNumber='" +
                phoneNumber + '\'' + '}';
    }
}
