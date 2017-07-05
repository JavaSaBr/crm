package com.ss.crm.service;

import com.ss.crm.db.entity.impl.token.BlankTokenEntity;
import com.ss.crm.db.entity.impl.user.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

/**
 * The service to work with blank token entities.
 *
 * @author JavaSaBr
 */
public interface BlankTokenService extends CrmService {

    /**
     * Create a new blank token for the user.
     *
     * @param type the type of a token.
     * @param user the user.
     * @return the new blank token.
     */
    @NotNull
    default <T extends BlankTokenEntity, U extends UserEntity> T createNewToken(@NotNull Class<T> type,
                                                                                @NotNull U user) {
        return createNewToken(type, user, null);
    }

    /**
     * Create a new blank token for the user.
     *
     * @param type the type of a token.
     * @param user the user.
     * @param handler the handler.
     * @return the new blank token.
     */
    @NotNull
    <T extends BlankTokenEntity, U extends UserEntity> T createNewToken(@NotNull Class<T> type, @NotNull U user,
                                                                        @Nullable BiConsumer<T, U> handler);
}
