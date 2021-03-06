package com.ss.jcrm.spring.base.template;

import com.ss.jcrm.spring.base.util.ResourceUtils;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class TemplateRegistry {

    @NotNull String template;

    public TemplateRegistry(@NotNull String basePath) {
        this.template = ResourceUtils.readAsString(basePath);
    }
}
