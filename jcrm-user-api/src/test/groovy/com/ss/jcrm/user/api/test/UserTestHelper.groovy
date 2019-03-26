package com.ss.jcrm.user.api.test

import com.ss.jcrm.user.api.Organization

interface UserTestHelper {

    Organization newOrg()

    void clearAllData()

    String nextUId()
}
