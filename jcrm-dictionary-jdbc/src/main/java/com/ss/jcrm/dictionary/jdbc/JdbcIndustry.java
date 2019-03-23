package com.ss.jcrm.dictionary.jdbc;

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
public class JdbcIndustry implements Industry {

    private final String name;

    private final long id;
}
