package com.ss.jcrm.security.web.service;

import com.ss.jcrm.dao.exception.ObjectNotFoundDaoException;
import com.ss.jcrm.security.web.exception.InvalidTokenException;
import com.ss.jcrm.security.web.exception.ExpiredTokenException;
import com.ss.jcrm.security.web.exception.MaxRefreshedTokenException;
import com.ss.jcrm.security.web.exception.PrematureTokenException;
import crm.user.api.User;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionException;

public interface TokenService {

    /**
     * @throws IllegalArgumentException if the length less than 1.
     */
    @NotNull String generateActivateCode(int length);

    @NotNull String generateNewToken(@NotNull User user);

    /**
     * @throws PrematureTokenException if the token is from future.
     * @throws InvalidTokenException if the token isn't valid.
     * @throws CompletionException -> ObjectNotFoundDaoException if the token's user cannot be found.
     */
    @NotNull Mono<@NotNull User> findUser(@NotNull String token);

    /**
     * @throws ExpiredTokenException if the token is expired.
     * @throws PrematureTokenException if the token is from future.
     * @throws InvalidTokenException if the token isn't valid.
     * @throws ObjectNotFoundDaoException if the token's user cannot be found.
     */
    @NotNull Mono<@NotNull User> findUserIfNotExpired(@NotNull String token);

    /**
     * @throws MaxRefreshedTokenException if the token reached max count of revokes.
     * @throws ExpiredTokenException if the token is expired.
     * @throws PrematureTokenException if the token is from future.
     * @throws InvalidTokenException if the token isn't valid.
     */
    @NotNull String refresh(@NotNull User user, @NotNull String token);
}
