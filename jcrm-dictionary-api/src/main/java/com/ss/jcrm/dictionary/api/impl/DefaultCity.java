package com.ss.jcrm.dictionary.api.impl;

import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@ToString
@AllArgsConstructor
@Getter(onMethod_ = @NotNull)
@EqualsAndHashCode(of = "id")
public class DefaultCity implements City {

    private final String name;
    private final Country country;

    private final long id;
}
