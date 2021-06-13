package com.ss.jcrm.dictionary.api.test

import com.ss.jcrm.dictionary.api.Country
import com.ss.jcrm.integration.test.db.config.TestHelper

interface DictionaryTestHelper extends TestHelper {

    Country newCountry()

    Country newCountry(String name)

    Country newCountry(String name, String flagCode, String phoneCode)
}
