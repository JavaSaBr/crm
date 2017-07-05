package com.ss.crm.endpoint.service.impl.user;

import org.jetbrains.annotations.Nullable;

/**
 * The request params of creating an user.
 *
 * @author JavaSaBr
 */
public class CreateUserParams {

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
     * @return the user name.
     */
    @Nullable
    public String getUsername() {
        return username;
    }

    /**
     * @param username the user name.
     */
    public void setUsername(@Nullable final String username) {
        this.username = username;
    }

    /**
     * @return the user password.
     */
    @Nullable
    public String getPassword() {
        return password;
    }

    /**
     * @param password the user password.
     */
    public void setPassword(@Nullable final String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CreateUserParams{" + "username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }
}
