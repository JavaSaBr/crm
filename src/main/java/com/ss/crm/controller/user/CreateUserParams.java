package com.ss.crm.controller.user;

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
    private String name;

    /**
     * The user password.
     */
    @Nullable
    private String password;

    /**
     * @return the user name.
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * @param name the user name.
     */
    public void setName(@Nullable final String name) {
        this.name = name;
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
        return "CreateUserParams{" + "name='" + name + '\'' + ", password='" + password + '\'' + '}';
    }
}
