package com.ss.crm.endpoint.service.impl.user;

import org.jetbrains.annotations.Nullable;

/**
 * The user credentials parameters.
 *
 * @author JavaSaBr
 */
public class UserCredentialsParams {

    /**
     * The user name.
     */
    @Nullable
    private String username;

    /**
     * The user password.
     */
    @Nullable
    private String password;

    /**
     * @param password the user password.
     */
    public void setPassword(@Nullable final String password) {
        this.password = password;
    }

    /**
     * @return the user password.
     */
    @Nullable
    public String getPassword() {
        return password;
    }

    /**
     * @param username the user name.
     */
    public void setUsername(@Nullable final String username) {
        this.username = username;
    }

    /**
     * @return the user name.
     */
    @Nullable
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "UserCredentialsParams{" + "username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }
}
