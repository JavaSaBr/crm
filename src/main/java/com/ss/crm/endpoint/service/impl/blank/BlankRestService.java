package com.ss.crm.endpoint.service.impl.blank;


import com.ss.crm.Routes;
import com.ss.crm.endpoint.service.BaseRestService;
import com.ss.crm.service.BlankTokenService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The REST controller to work with blanks.
 *
 * @author JavaSaBr
 */
@Service
@RequestMapping("/" + Routes.BLANK)
public class BlankRestService extends BaseRestService {

    @NotNull
    protected final BlankTokenService blankTokenService;

    @Autowired
    public BlankRestService(@NotNull final BlankTokenService blankTokenService) {
        this.blankTokenService = blankTokenService;
    }
}
