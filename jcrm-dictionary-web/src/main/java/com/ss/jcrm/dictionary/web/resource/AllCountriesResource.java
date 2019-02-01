package com.ss.jcrm.dictionary.web.resource;

import com.ss.jcrm.web.resources.RestResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllCountriesResource implements RestResource {

    private CountryResource[] countries;

    public AllCountriesResource(@NotNull List<CountryResource> resources) {
        this.countries = resources.toArray(CountryResource[]::new);
    }
}
