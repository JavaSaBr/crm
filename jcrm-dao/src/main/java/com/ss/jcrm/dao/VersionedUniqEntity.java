package com.ss.jcrm.dao;

public interface VersionedUniqEntity extends UniqEntity {

    int getVersion();

    void setVersion(int version);
}
