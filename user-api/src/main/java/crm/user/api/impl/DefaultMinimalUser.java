package crm.user.api.impl;

import com.ss.jcrm.security.AccessRole;
import crm.user.api.MinimalUser;

import java.util.Set;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "id")
public record DefaultMinimalUser(
    long id,
    long orgId,
    String email,
    Set<AccessRole> roles) implements MinimalUser {}