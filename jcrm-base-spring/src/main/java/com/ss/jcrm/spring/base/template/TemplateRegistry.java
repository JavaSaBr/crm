package com.ss.jcrm.spring.base.template;

import com.ss.jcrm.spring.base.util.ResourceUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class TemplateRegistry {

    @Getter
    private final String template;

    public TemplateRegistry(@NotNull String basePath) {
        this.template = ResourceUtils.readAsString(basePath);
    }
}
