package com.ss.crm.controller.user;

import static java.util.Objects.requireNonNull;
import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
public class UserInfo {

    /**
     * The user name.
     */
    private String name;

    /**
     * @param name the user name.
     */
    public void setName(@NotNull final String name) {
        this.name = name;
    }

    /**
     * @return the user name.
     */
    @NotNull
    public String getName() {
        return requireNonNull(name, "The name should be non null.");
    }

    @Override
    public String toString() {
        return "UserInfo{" + "name='" + name + '\'' + '}';
    }
}
