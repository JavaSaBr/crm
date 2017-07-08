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

    @Version
    @Column(name = "version")
    protected int version;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BaseEntity that = (BaseEntity) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
