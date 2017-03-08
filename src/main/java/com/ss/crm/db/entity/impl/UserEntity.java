package com.ss.crm.db.entity.impl;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author JavaSaBr
 */
@Entity
@Table(name = "USER_ENTITY", indexes = {
        @Index(columnList = "name", name = "user_name_index")
})
public class UserEntity extends BaseEntity {

    /**
     * The user name.
     */
    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    /**
     * The user password.
     */
    @Column(name = "password", nullable = false)
    private byte[] password;

    /**
     * The user password salt.
     */
    @Column(name = "password_salt", nullable = false)
    private byte[] passwordSalt;

    /**
     * @return the user name.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @param name the user name.
     */
    public void setName(@NotNull final String name) {
        this.name = name;
    }

    /**
     * @return the user password.
     */
    @NotNull
    public byte[] getPassword() {
        return password;
    }

    /**
     * @param password the user password.
     */
    public void setPassword(@NotNull final byte[] password) {
        this.password = password;
    }

    /**
     * @return the user password salt.
     */
    @NotNull
    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * @param passwordSalt the user password salt.
     */
    public void setPasswordSalt(@NotNull final byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    @Override
    public String toString() {
        return "UserEntity{" + "name='" + name + '\'' + '}';
    }
}
