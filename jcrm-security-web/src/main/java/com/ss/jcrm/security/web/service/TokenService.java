package com.ss.jcrm.security.web.service;

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.jcrm.security.web.exception.InvalidTokenException;
import com.ss.jcrm.security.web.exception.TokenExpiredException;
import com.ss.jcrm.user.api.User;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface TokenService {

    /**
     * @throws IllegalArgumentException if the length less than 1.
     */
    @NotNull String generateActivateCode(int length);

    @NotNull String generateNewToken(@NotNull User user);

    /**
     * @throws TokenExpiredException if the token is expired.
     * @throws InvalidTokenException if the token isn't valid.
     * @throws ObjectNotFoundDaoException if the token's user cannot be found.
     */
    @NotNull User findUserIfNotExpired(@NotNull String token);

    /**
     * @throws TokenExpiredException if the token is expired.
     * @throws InvalidTokenException if the token isn't valid.
     * @throws CompletionException -> ObjectNotFoundDaoException if the token's user cannot be found.
     */
    @NotNull CompletableFuture<@NotNull User> findUserIfNotExpiredAsync(@NotNull String token);

    boolean isExpired(@NotNull String token);
}
