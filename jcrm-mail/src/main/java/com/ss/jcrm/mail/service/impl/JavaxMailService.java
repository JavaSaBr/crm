package com.ss.jcrm.mail.service.impl;

import static java.util.concurrent.CompletableFuture.runAsync;
import com.ss.jcrm.mail.exception.MailException;
import com.ss.jcrm.mail.service.MailService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.concurrent.*;

@Log4j2
public class JavaxMailService implements MailService {

    private final Executor executor;
    private final Session session;
    private final InternetAddress from;

    public JavaxMailService(@NotNull Environment env) {

        var host = env.getRequiredProperty("javax.mail.smtp.host");
        var port = env.getRequiredProperty("javax.mail.smtp.port");

        var prop = new Properties();
        prop.put("mail.smtp.auth", env.getProperty("javax.mail.smtp.auth", "true"));
        prop.put("mail.smtp.starttls.enable", env.getProperty("javax.mail.smtp.starttls.enable", "true"));
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.socketFactory.port", port);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.ssl.trust", env.getRequiredProperty("javax.mail.smtp.ssl.trust"));

        var username = env.getRequiredProperty("javax.mail.username");
        var password = env.getRequiredProperty("javax.mail.password");

        this.session = Session.getInstance(prop, new Authenticator() {

            @Override
            protected @NotNull PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            this.from = new InternetAddress(env.getRequiredProperty("javax.mail.smtp.from"));
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }

        log.info("Initialized mail services with settings:");
        log.info("host : {}:{}", host, port);
        log.info("from : {}", from);

        this.executor = new ThreadPoolExecutor(
            env.getProperty("javax.mail.executor.min.threads", int.class, 1),
            env.getProperty("javax.mail.executor.max.threads", int.class, 4),
            env.getProperty("javax.mail.executor.keep.alive", int.class, 120),
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public JavaxMailService(
        @NotNull String host,
        int port,
        boolean smtpAuth,
        boolean startTtls,
        @NotNull String sslTrust,
        @NotNull String username,
        @NotNull String password,
        @NotNull String smtpFrom,
        int minThreads,
        int maxThreads,
        int keepAlive
    ) {

        var prop = new Properties();
        prop.put("mail.smtp.auth", String.valueOf(smtpAuth));
        prop.put("mail.smtp.starttls.enable", String.valueOf(startTtls));
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.socketFactory.port", port);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.ssl.trust", sslTrust);

        this.session = Session.getInstance(prop, new Authenticator() {

            @Override
            protected @NotNull PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            this.from = new InternetAddress(smtpFrom);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }

        log.info("Initialized mail services with settings:");
        log.info("host : {}:{}", host, port);
        log.info("from : {}", from);

        this.executor = new ThreadPoolExecutor(
            minThreads,
            maxThreads,
            keepAlive,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Override
    public void send(@NotNull String email, @NotNull String subject, @NotNull String content) {

        try {

            var message = new MimeMessage(session);
            message.setFrom(from);
            message.setRecipients(RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);

            var mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(content, "text/html");

            var multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<Void> sendAsync(
        @NotNull String email,
        @NotNull String subject,
        @NotNull String content
    ) {
        return runAsync(() -> send(email, subject, content), executor);
    }
}
