package com.ss.jcrm.dictionary.api.impl;

import com.ss.jcrm.dictionary.api.Industry;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultIndustry implements Industry {

    @NotNull String name;

    long id;
}
