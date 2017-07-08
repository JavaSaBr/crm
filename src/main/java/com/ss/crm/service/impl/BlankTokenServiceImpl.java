package com.ss.crm.service.impl;

import static com.ss.rlib.util.ObjectUtils.notNull;
import static java.time.ZonedDateTime.now;
import com.ss.crm.db.entity.impl.token.BlankTokenEntity;
import com.ss.crm.db.entity.impl.user.UserEntity;
import com.ss.crm.db.repository.BlankTokenRepository;
import com.ss.crm.service.BlankTokenService;
import com.ss.rlib.util.ClassUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.BiConsumer;

/**
 * The implementation of the {@link BlankTokenService}.
 *
 * @author JavaSaBr
 */
@Transactional
@Component("blankTokenService")
public class BlankTokenServiceImpl extends AbstractCrmService implements BlankTokenService {

    private static final int TOKEN_HOURS = 24 * 3;

    @NotNull
    private final BlankTokenRepository blankTokenRepository;

    @Autowired
    public BlankTokenServiceImpl(@NotNull final BlankTokenRepository blankTokenRepository) {
        this.blankTokenRepository = blankTokenRepository;
    }

    @NotNull
    @Override
    public <T extends BlankTokenEntity, U extends UserEntity> T createNewToken(@NotNull final Class<T> type,
                                                                               @NotNull final U user,
                                                                               @Nullable final BiConsumer<T, U> handler) {

        final Key key = MacProvider.generateKey();
        final ZonedDateTime expiry = now().plusHours(TOKEN_HOURS);

        final String compactJws = Jwts.builder()
                .setNotBefore(Date.from(expiry.toInstant()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        final T tokenEntity = ClassUtils.newInstance(type);
        tokenEntity.setUserId(notNull(user.getId()));
        tokenEntity.setExpiry(expiry);
        tokenEntity.setToken(compactJws);

        if(handler != null) {
            handler.accept(tokenEntity, user);
        }

        blankTokenRepository.save(tokenEntity);

        return tokenEntity;
    }
}
