package com.ss.jcrm.dictionary.web.config;

import com.ss.jcrm.dictionary.jdbc.config.JdbcDictionaryConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    JdbcDictionaryConfig.class
})
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.ss.jcrm.dictionary.web")
public class DictionaryConfig {
}
