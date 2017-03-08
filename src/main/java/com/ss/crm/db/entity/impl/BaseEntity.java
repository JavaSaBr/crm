package com.ss.crm.db.entity.impl;

import com.ss.crm.db.entity.HasId;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;

/**
 * The base implementation of a database entity.
 *
 * @author JavaSaBr
 */
@MappedSuperclass
public abstract class BaseEntity implements HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, precision = 15)
    protected Long id;

    public BaseEntity() {
    }

    public BaseEntity(@Nullable final Long id) {
        this.id = id;
    }

    @Nullable
    @Override
    public Long getId() {
        return id;
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
