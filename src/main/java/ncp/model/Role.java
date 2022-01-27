package ncp.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    CONSUMER,
    PROVIDER;

    @Override
    public String getAuthority() {
        return name();
    }
}
