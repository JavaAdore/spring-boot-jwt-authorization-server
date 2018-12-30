package com.sample.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.*;

@RestController
@RequestMapping("social/")
public class SocialLoginController {


    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private AuthorizationServerEndpointsConfiguration configuration;




    @RequestMapping("/facebook")
    public OAuth2AccessToken test(@RequestParam("facebookAccessToken") String facebookAccessToken)
    {

        Facebook facebook = new FacebookTemplate(facebookAccessToken);
        String[] fields = { "id", "email", "first_name", "last_name" };

        org.springframework.social.facebook.api.User u=    facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
        System.out.println(u);
        CustomUserDetails user = new CustomUserDetails("mahmoud","eltaieb",new HashSet<>());
        user.setApplicationUserId("123321");
        List<Role> roles=  new LinkedList<>();
        Role r = new Role();
        r.name="user";
        roles.add(r);;

        List<String> scope = new ArrayList<>();
        scope.add("read");
        scope.add("write");

        return generateOAuth2AccessToken(user,roles,scope);
    }

     public OAuth2AccessToken generateOAuth2AccessToken(User user, List<Role> roles, List<String> scopes) {

        Map<String, String> requestParameters = new HashMap<String, String>();
        Map<String, Serializable> extensionProperties = new HashMap<String, Serializable>();

        boolean approved = true;
        Set<String> responseTypes = new HashSet<String>();
        responseTypes.add("code");

        // Authorities
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role : roles)
            authorities.add(new SimpleGrantedAuthority(role.getName()));

        OAuth2Request oauth2Request = new OAuth2Request(requestParameters, "client1", authorities, approved, new HashSet<String>(scopes), new HashSet<String>(Arrays.asList("resourceIdTest")), null, responseTypes, extensionProperties);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, "eltaieb", authorities);
         authenticationToken.setDetails(user);

        OAuth2Authentication auth = new OAuth2Authentication(oauth2Request, authenticationToken);

        AuthorizationServerTokenServices tokenService = configuration.getEndpointsConfigurer().getTokenServices();

        OAuth2AccessToken token = tokenService.createAccessToken(auth);

        return token;
    }

    class Role
    {
        String name;
        String getName()
        {
            return name;
        }
    }


}
