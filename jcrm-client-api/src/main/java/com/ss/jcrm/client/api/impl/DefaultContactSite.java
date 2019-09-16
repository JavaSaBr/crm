package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ContactSite;
import com.ss.jcrm.client.api.SiteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultContactSite implements ContactSite {

    private String url;
    private SiteType type;
}
