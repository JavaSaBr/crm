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
public class CountryResource implements RestResource {

    private String name;
    private String flagCode;
    private String phoneCode;
}
