package com.ss.jcrm.dictionary.web.test.controller

import com.ss.jcrm.base.utils.Reloadable
import com.ss.jcrm.dictionary.web.resource.AllCountriesResource
import com.ss.jcrm.dictionary.web.resource.CountryResource
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService
import com.ss.jcrm.dictionary.web.test.DictionarySpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize

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

            WebTestClient.ResponseSpec response = client.get()
                .uri("/dictionary/countries")
                .exchange()

        then:

            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                    .jsonPath('$.countries')
                        .value(hasSize(3))
                    .jsonPath('$.countries[*].name')
                        .value(containsInAnyOrder(
                            country1.name,
                            country2.name,
                            country3.name)
                        )
    }
}
