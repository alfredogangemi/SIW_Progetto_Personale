package it.uniroma3.siw.booking.oauth;


import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User user;

    public CustomOAuth2User(OAuth2User user) {
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return user.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getName() {
        return user.getName();
    }

    public String getFullName() {
        return user.getAttribute("name");
    }

    public String getLogin() {
        String username = user.getAttribute("login");
        if (StringUtils.isBlank(username)) {
            username = this.getEmail();
        }
        return username;
    }

    public String getEmail() {return user.getAttribute("email");}


    public String getFirstName() {
        return user.getAttribute("given_name");
    }


    public String getLastName() {
        return user.getAttribute("family_name");
    }



}
