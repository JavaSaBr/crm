package com.ss.crm.db.entity.impl.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * The admin user.
 *
 * @author JavaSaBr
 */
@Entity
@DiscriminatorValue(value = "4")
public class AdminEntity extends UserEntity {
}
