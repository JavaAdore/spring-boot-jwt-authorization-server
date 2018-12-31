package com.sample.security.jwt;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;


import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.*;

@RestController
@RequestMapping("social/")
public class SocialLoginController {



    @Autowired
    private AuthorizationServerEndpointsConfiguration configuration;


    @Value("${google.client.id}")
    private String googleClientId;


    @RequestMapping("/facebook")
    public OAuth2AccessToken test(@RequestParam("facebookAccessToken") String facebookAccessToken)
    {

        Facebook facebook = new FacebookTemplate(facebookAccessToken);
        String[] fields = { "id", "email", "first_name", "last_name" };

        org.springframework.social.facebook.api.User u=    facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
        System.out.println(u);
        CustomUserDetails user = new CustomUserDetails("mahmoud","eltaieb",new HashSet<>());
        user.setApplicationUserId(fields[0]);



        return generateOAuth2AccessToken(user);
    }


    @RequestMapping("/google")
    public OAuth2AccessToken google(@RequestParam("clientIdToken") String clientIdToken) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(googleClientId))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

// (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = verifier.verify(clientIdToken);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

             CustomUserDetails user = new CustomUserDetails("mahmoud","eltaieb",new HashSet<>());
            user.setApplicationUserId("123456");
            return generateOAuth2AccessToken(user);
            // Use or store profile information
            // ...

        } else {
            System.out.println("Invalid ID token.");
        }
        return null;
    }

    public OAuth2AccessToken generateOAuth2AccessToken(User user ) {

        Map<String, String> requestParameters = new HashMap<String, String>();
        Map<String, Serializable> extensionProperties = new HashMap<String, Serializable>();

        List<String> scopes = new ArrayList<>();
        scopes.add("read");
        scopes.add("write");

        List<Role> roles=  new LinkedList<>();
        Role r = new Role();
        r.name="user";
        roles.add(r);;



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
