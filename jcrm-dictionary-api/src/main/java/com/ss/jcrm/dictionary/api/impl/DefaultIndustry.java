package com.ss.jcrm.dictionary.api.impl;

import com.ss.jcrm.dictionary.api.Industry;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@ToString
@AllArgsConstructor
@Getter(onMethod_ = @NotNull)
@EqualsAndHashCode(of = "id")
public class DefaultIndustry implements Industry {

    private final String name;

    private final long id;
}
