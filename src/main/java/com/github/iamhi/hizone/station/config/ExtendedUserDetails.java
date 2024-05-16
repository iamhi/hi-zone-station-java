package com.github.iamhi.hizone.station.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class ExtendedUserDetails extends User  {

    private String uuid;

    public ExtendedUserDetails(
        String uuid,
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.uuid = uuid;
    }
}
