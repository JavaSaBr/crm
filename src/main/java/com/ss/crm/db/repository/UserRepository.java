package com.ss.crm.db.repository;

import com.ss.crm.db.entity.impl.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author JavaSaBr
 */
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
}
