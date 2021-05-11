package com.ss.jcrm.dictionary.api.impl;

import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultCity implements City {

    @NotNull String name;
    @NotNull Country country;

    long id;
}
