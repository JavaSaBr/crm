package com.ss.jcrm.dictionary.api.test

import com.ss.jcrm.dictionary.api.Country

interface DictionaryTestHelper {

    Country newCountry()

    Country newCountry(String name)

    Country newCountry(String name, String flagCode, String phoneCode)

    void clearAllData()
}
