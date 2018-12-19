package com.ss.jcrm.dictionary.api;

import org.jetbrains.annotations.NotNull;

public interface Country {

    long getId();

    @NotNull String getName();

    @NotNull String getFlagCode();

    @NotNull String getPhoneCode();
}
