package com.ss.crm.service.impl;

import static com.ss.rlib.util.Utils.get;
import static java.lang.ThreadLocal.withInitial;
import static java.nio.file.Files.readAllBytes;
import static java.util.Objects.requireNonNull;
import com.ss.crm.service.MailService;
import com.ss.rlib.util.dictionary.ConcurrentObjectDictionary;
import com.ss.rlib.util.dictionary.DictionaryFactory;
import com.ss.rlib.util.dictionary.DictionaryUtils;
import com.ss.rlib.util.dictionary.ObjectDictionary;
import com.ss.rlib.util.ref.Reference;
import com.ss.rlib.util.ref.ReferenceFactory;
import com.ss.rlib.util.ref.ReferenceType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The base implementation of the {@link MailService},
 *
 * @author JavaSaBr
 */
@Component("mailService")
public class MailServiceImpl extends AbstractCrmService implements MailService {

    /**
     * The thread local context.
     */
    @NotNull
    private static final ThreadLocal<ObjectDictionary<String, Object>> TL_CONTEXT = withInitial(DictionaryFactory::newObjectDictionary);

    /**
     * THe table of all templates.
     */
    @NotNull
    private final ConcurrentObjectDictionary<String, String> templates;

    /**
     * The executor service.
     */
    @NotNull
    private final ExecutorService executorService;

    /**
     * The mail sender.
     */
    @NotNull
    private final MailSender mailSender;

    @Autowired
    public MailServiceImpl(@NotNull final MailSender mailSender) {
        this.mailSender = mailSender;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        this.templates = DictionaryFactory.newConcurrentAtomicObjectDictionary();
    }

    @NotNull
    @Override
    public ObjectDictionary<String, Object> threadLocalContext() {
        return TL_CONTEXT.get();
    }

    @Override
    public String template(@NotNull final String id) {

        final String exists = DictionaryUtils.getInReadLock(templates, id, ObjectDictionary::get);
        if(exists != null) return exists;

        final long stamp = templates.writeLock();
        try {

            return templates.get(id, (key) -> {

                final ClassLoader classLoader = MailServiceImpl.class.getClassLoader();
                final URL resource = classLoader.getResource("/templates/ru/mail/" + key);

                if(resource == null) {
                    throw new RuntimeException("not found a template for the ID " + key);
                }

                return get(resource, url -> new String(readAllBytes(Paths.get(url.toURI()))));
            });

        } finally {
            templates.writeUnlock(stamp);
        }
    }

    @Override
    public void sendMail(@NotNull final String email, @NotNull final String template,
                         @NotNull final ObjectDictionary<String, Object> context) {
        executorService.submit(() -> send(email, template, context));
    }

    private void send(@NotNull final String email, @NotNull final String template,
                      @NotNull final ObjectDictionary<String, Object> context) {

        final Reference reference = ReferenceFactory.takeFromTLPool(ReferenceType.OBJECT);
        reference.setObject(template);
        try {

            context.forEach(reference, this::setVarToTemplate);

            final SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(String.valueOf(context.get(SUBJECT)));
            message.setText((String) reference.getObject());

            mailSender.send(message);

        } finally {
            reference.release();
        }
    }

    private void setVarToTemplate(@NotNull final Reference ref, @NotNull final String name,
                                  @NotNull final Object value) {

        if (!name.startsWith("${")) {
            return;
        }

        final String text = (String) requireNonNull(ref.getObject());
        if (!text.contains(name)) return;

        ref.setObject(text.replace(name, String.valueOf(value)));
    }
}
