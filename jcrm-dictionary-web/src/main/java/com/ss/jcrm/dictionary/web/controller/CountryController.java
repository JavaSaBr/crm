package com.ss.jcrm.dictionary.web.controller;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import com.ss.jcrm.dictionary.web.resource.AllCountriesResource;
import com.ss.jcrm.dictionary.web.resource.CountryResource;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor_ = @Autowired)
public class CountryController {

    private final CachedDictionaryService<CountryResource, AllCountriesResource> countryDictionaryService;

    @GetMapping("/dictionary/countries")
    @NotNull ResponseEntity<AllCountriesResource> getAll() {
        return ok(countryDictionaryService.getAll());
    }

    @GetMapping("/dictionary/country/{id}")
    @NotNull ResponseEntity<CountryResource> get(@PathVariable("id") long id) {

        var resource = countryDictionaryService.getById(id);

        if (resource != null) {
            return ok(resource);
        } else {
            return notFound().build();
        }
    }

    @GetMapping("/dictionary/name/{name}")
    @NotNull ResponseEntity<CountryResource> getByName(@NotNull @PathVariable("name") String name) {

        var resource = countryDictionaryService.getByName(name.toLowerCase());

        if (resource != null) {
            return ResponseEntity.ok(resource);
        } else {
            return notFound().build();
        }
    }
}
