package com.ss.crm.endpoint.service.impl.blank;


import com.ss.crm.Routes;
import com.ss.crm.service.BlankTokenService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The REST controller to work with blanks of customer registration.
 *
 * @author JavaSaBr
 */
@Service
@RequestMapping("/" + Routes.BLANK_CUSTOMER_REGISTER)
public class CustomerRegisterBlankRestService extends BlankRestService {

    public CustomerRegisterBlankRestService(@NotNull final BlankTokenService blankTokenService) {
        super(blankTokenService);
    }
}
