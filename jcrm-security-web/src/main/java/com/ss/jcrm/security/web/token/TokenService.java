package com.ss.jcrm.security.web.token;

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.jcrm.security.web.token.exception.InvalidTokenException;
import com.ss.jcrm.security.web.token.exception.TokenExpiredException;
import com.ss.jcrm.user.api.User;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface TokenService {

    @NotNull String generateNewToken(@NotNull User user);

    /**
     * @throws TokenExpiredException if the token is expired.
     * @throws ObjectNotFoundDaoException if the token's user cannot be found.
     * @throws InvalidTokenException if the token isn't valid.
     */
    @NotNull User findUserIfNotExpired(@NotNull String token);

    /**
     * @throws TokenExpiredException if the token is expired.
     * @throws ObjectNotFoundDaoException if the token's user cannot be found.
     * @throws InvalidTokenException if the token isn't valid.
     */
    @NotNull CompletableFuture<@NotNull User> findUserIfNotExpiredAsync(@NotNull String token);

    boolean isExpired(@NotNull String token);
}
