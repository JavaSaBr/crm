package com.ss.jcrm.dictionary.api.test

import com.ss.jcrm.dictionary.api.Country
import integration.test.db.DbTestHelper

interface DictionaryDbTestHelper extends DbTestHelper {

    Country newCountry()

    Country newCountry(String name)

    Country newCountry(String name, String flagCode, String phoneCode)
}
