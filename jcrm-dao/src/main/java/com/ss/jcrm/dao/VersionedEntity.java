package com.ss.jcrm.dao;

public interface VersionedEntity extends Entity {

    int getVersion();

    void setVersion(int version);
}
