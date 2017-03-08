package com.ss.crm.db.entity;

import org.jetbrains.annotations.Nullable;

/**
 * The interface to mark an object that it has an ID.
 *
 * @author JavaSaBr
 */
public interface HasId {

    /**
     * Get an ID of this entity.
     *
     * @return the ID.
     */
    @Nullable
    Long getId();

    /**
     * Set an ID to this entity.
     *
     * @param id the ID.
     */
    void setId(@Nullable Long id);
}
