package com.ss.jcrm.dictionary.web.resource;

import com.ss.jcrm.web.resources.RestResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllCountriesResource implements RestResource {

    private CountryResource[] countries;
}
