package com.ss.crm.db.entity.impl.token;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author JavaSABr
 */
@Entity
@DiscriminatorValue(value = "2")
public class CustomerRegisterBlankTokenEntity extends BlankTokenEntity {
}
