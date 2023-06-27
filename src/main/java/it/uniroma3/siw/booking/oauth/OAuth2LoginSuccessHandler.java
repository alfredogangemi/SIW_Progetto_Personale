package it.uniroma3.siw.booking.oauth;


import it.uniroma3.siw.booking.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    protected final UserService userService;

    @Autowired
    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("login with oAuth..");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String loginName = oAuth2User.getLogin();
        log.debug("Attributes {}", oAuth2User.getAttributes());
        boolean exists = userService.existsByUsername(loginName);
        if (!exists) {
            log.info("Creating new user {} from oAuth ", loginName);
            userService.registerOAuthUser(oAuth2User);
        }
        log.info("redirecting to home....");
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        response.sendRedirect("/success");

    }



}
