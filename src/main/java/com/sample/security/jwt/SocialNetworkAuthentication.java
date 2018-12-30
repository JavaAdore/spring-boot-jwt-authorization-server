package com.sample.security.jwt;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class SocialNetworkAuthentication extends UsernamePasswordAuthenticationToken {

    private SocialNetworkType socialNetworkType;
    private String  token;
    public SocialNetworkAuthentication(String token,SocialNetworkType socialNetworkType) {
        super(token,"");
        this.socialNetworkType= socialNetworkType;
        this.token = token;
    }
    public SocialNetworkAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public SocialNetworkAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }


}
