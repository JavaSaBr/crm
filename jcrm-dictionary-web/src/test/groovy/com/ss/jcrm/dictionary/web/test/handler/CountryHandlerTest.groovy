package com.ss.jcrm.dictionary.web.test.handler

import com.ss.jcrm.dictionary.web.resource.CountryOutResource
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService
import com.ss.jcrm.dictionary.web.test.DictionarySpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static com.ss.jcrm.web.exception.CommonErrors.ID_NOT_PRESENTED
import static com.ss.jcrm.web.exception.CommonErrors.ID_NOT_PRESENTED_MESSAGE
import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is

class CountryHandlerTest extends DictionarySpecification {

    @Autowired
    private CachedDictionaryService<CountryOutResource, CountryOutResource[]> countryDictionaryService

    def "should get all countries"() {

        given:

            def country1 = dictionaryTestHelper.newCountry("country1")
            def country2 = dictionaryTestHelper.newCountry("country2")
            def country3 = dictionaryTestHelper.newCountry("country3")

            reloadableServices.each {
                it.reload()
            }

        when:
            def response = webClient.get()
                .url("$contextPath/countries")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$').value(hasSize(3))
                    .jsonPath('$[*].name').value(containsInAnyOrder(
                        country1.name,
                        country2.name,
                        country3.name))
    }
    
    def "should not get not exists country"() {
        when:
            def response = webClient.get()
                .url("$contextPath/country/id/1")
                .exchange()
        then:
            response.expectStatus()
                .isNotFound()
        when:
            response = webClient.get()
                .url("$contextPath/country/name/notexist")
                .exchange()
        then:
            response.expectStatus()
                .isNotFound()
    }
    
    def "should get bad request with wrong id format"() {
        when:
            def response = webClient.get()
                .url("$contextPath/country/id/incorrectid")
                .exchange()
        then:
            response.expectStatus().isBadRequest()
                .verifyErrorResponse(ID_NOT_PRESENTED, ID_NOT_PRESENTED_MESSAGE)
    }
    
    def "should get country by id"() {
        
        given:

            def country = dictionaryTestHelper.newCountry("country1")

            reloadableServices.each {
                it.reload()
            }

        when:
            def response = webClient.get()
                .url("$contextPath/country/id/$country.id")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$.name').value(is(country.name))
                    .jsonPath('$.id').value(is((int) country.id))
    }
    
    def "should get country by name"() {
        
        given:

            def country = dictionaryTestHelper.newCountry("test")

            reloadableServices.each {
                it.reload()
            }

        when:
            def response = webClient.get()
                .url("$contextPath/country/name/$country.name")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath('$.name').value(is(country.name))
                    .jsonPath('$.id').value(is((int) country.id))
    }

    def "should get all countries with CORS headers"() {

        given:

            def country1 = dictionaryTestHelper.newCountry("country1")
            def country2 = dictionaryTestHelper.newCountry("country2")
            def country3 = dictionaryTestHelper.newCountry("country3")

            reloadableServices.each {
                it.reload()
            }

        when:
            def response = webClient.get()
                .url("$contextPath/countries")
                .headerValue("Origin", "http://localhost")
                .exchange()
        then:
            response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Access-Control-Allow-Origin")
                .expectBody()
                    .jsonPath('$').value(hasSize(3))
                    .jsonPath('$[*].name').value(containsInAnyOrder(
                        country1.name,
                        country2.name,
                        country3.name))
    }
}
