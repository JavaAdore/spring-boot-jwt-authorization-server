package com.sample.security.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public class SocialNetworkAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SocialNetworkAuthentication socialNetworkAuthentication = (SocialNetworkAuthentication) authentication;

        if(SocialNetworkType.FACEBOOK == socialNetworkAuthentication.getSocialNetworkType())
        {
            socialNetworkAuthentication.getToken();


        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(SocialNetworkAuthentication.class);
    }
}
