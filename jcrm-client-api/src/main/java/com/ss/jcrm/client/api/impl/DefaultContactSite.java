package com.ss.jcrm.client.api.impl;

import com.ss.jcrm.client.api.ClientSite;
import com.ss.jcrm.client.api.SiteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultContactSite implements ClientSite {

    private String url;
    private SiteType type;
}
