package com.ss.jcrm.integration.test.smtp;

import org.testcontainers.containers.GenericContainer;

public class FakeSMTPContainer extends GenericContainer<FakeSMTPContainer> {

    public static final int SMTP_PORT = 5025;

    public final String username = "test";
    public final String password = "test";

    public FakeSMTPContainer() {
        super("kurzdigital/fake-smtp");
    }

    @Override
    protected void configure() {
        addExposedPort(SMTP_PORT);
        addEnv("SMTP_USER", username);
        addEnv("SMTP_PASSWORD", username);
    }
}
