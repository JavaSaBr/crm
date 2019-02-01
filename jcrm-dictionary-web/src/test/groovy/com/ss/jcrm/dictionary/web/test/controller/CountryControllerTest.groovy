package com.ss.jcrm.dictionary.web.test.controller

import com.ss.jcrm.base.utils.Reloadable
import com.ss.jcrm.dictionary.web.resource.AllCountriesResource
import com.ss.jcrm.dictionary.web.resource.CountryResource
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService
import com.ss.jcrm.dictionary.web.test.DictionarySpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class CountryControllerTest extends DictionarySpecification {

    @Autowired
    private CachedDictionaryService<CountryResource, AllCountriesResource> countryDictionaryService

    def "should get all countries"() {

        given:

            def country1 = dictionaryTestHelper.newCountry("country1")
            def country2 = dictionaryTestHelper.newCountry("country2")
            def country3 = dictionaryTestHelper.newCountry("country3")

            (countryDictionaryService as Reloadable).reload()

        when:
            def response = mvc.perform(get("/countries"))
        then:

            response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$.countries', hasSize(3)))
                .andExpect(jsonPath('$.countries[*].name', containsInAnyOrder(country1.name, country2.name, country3.name)))
                .andReturn()
    }
}
