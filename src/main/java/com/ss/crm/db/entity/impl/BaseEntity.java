package com.ss.crm.db.entity.impl;

import com.ss.crm.db.entity.HasId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * The base implementation of a database entity.
 *
 * @author JavaSaBr
 */
@MappedSuperclass
public abstract class BaseEntity implements HasId {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, precision = 15)
    protected Long id;

    public BaseEntity() {
    }

    public BaseEntity(@Nullable final Long id) {
        this.id = id;
    }

    @NotNull
    @Override
    public Long getId() {
        return Objects.requireNonNull(id, "The ID is null.");
    }

    @Override
    public void setId(@Nullable final Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseEntity{" + "id=" + id + '}';
    }
}
