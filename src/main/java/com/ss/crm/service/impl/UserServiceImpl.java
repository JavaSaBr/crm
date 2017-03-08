package com.ss.crm.service.impl;

import com.ss.crm.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JavaSaBr
 */
@Transactional
@Component("userService")
public class UserServiceImpl extends AbstractCrmService implements UserService {
}
