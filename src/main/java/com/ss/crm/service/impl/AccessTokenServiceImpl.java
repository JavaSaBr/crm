package com.ss.crm.service.impl;

import static java.time.ZonedDateTime.now;
import static java.util.Objects.requireNonNull;
import com.ss.crm.db.entity.impl.AccessTokenEntity;
import com.ss.crm.db.entity.impl.UserEntity;
import com.ss.crm.db.repository.AccessTokenRepository;
import com.ss.crm.db.repository.UserRepository;
import com.ss.crm.service.AccessTokenService;
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

/**
 * The implementation of the {@link AccessTokenService}.
 *
 * @author JavaSaBr
 */
@Transactional
@Component("accessTokenService")
public class AccessTokenServiceImpl extends AbstractCrmService implements AccessTokenService {

    private static final int ACCESS_TOKEN_HOURS = 4;

    @NotNull
    private final AccessTokenRepository accessTokenRepository;

    @NotNull
    private final UserRepository userRepository;

    @Autowired
    public AccessTokenServiceImpl(@NotNull final AccessTokenRepository accessTokenRepository,
                                  @NotNull final UserRepository userRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.userRepository = userRepository;
    }

    @Nullable
    @Override
    public AccessTokenEntity getLastToken(@NotNull final UserEntity user) {
        return accessTokenRepository.findOneByUserIdOrderByExpiryDesc(requireNonNull(user.getId()));
    }

    @NotNull
    @Override
    public AccessTokenEntity createNewToken(@NotNull final UserEntity user) {

        final Key key = MacProvider.generateKey();
        final ZonedDateTime expiry = now().plusHours(ACCESS_TOKEN_HOURS);

        final String compactJws = Jwts.builder()
                .setSubject(user.getName())
                .setNotBefore(Date.from(expiry.toInstant()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        final AccessTokenEntity tokenEntity = new AccessTokenEntity();
        tokenEntity.setUserId(requireNonNull(user.getId()));
        tokenEntity.setExpiry(expiry);
        tokenEntity.setToken(compactJws);

        return accessTokenRepository.save(tokenEntity);
    }

    @Nullable
    @Override
    @Transactional
    public UserEntity findUserByToken(@NotNull final String token) {

        final AccessTokenEntity accessTokenEntity = accessTokenRepository.findOneByToken(token);

        if (accessTokenEntity == null) {
            return null;
        } else if (accessTokenEntity.isExpired()) {
            accessTokenRepository.delete(accessTokenEntity);
            return null;
        }

        return userRepository.findOne(accessTokenEntity.getUserId());
    }
}
