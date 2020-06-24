package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.user.api.MinimalUser;

public interface MinimalUserDao extends Dao<MinimalUser> {

    MinimalUser[] EMPTY_USERS = new MinimalUser[0];
}
