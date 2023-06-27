package it.uniroma3.siw.booking.controller;

import it.uniroma3.siw.booking.dto.UserDetailsDto;
import it.uniroma3.siw.booking.model.Credentials;
import it.uniroma3.siw.booking.oauth.CustomOAuth2User;
import it.uniroma3.siw.booking.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {


    protected final CredentialsService credentialsService;

    @Autowired
    public GlobalController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @ModelAttribute("userDetails")
    public UserDetailsDto getUser() {
        Credentials credentials = this.getCredentials();
        if (credentials != null) {
            return UserDetailsDto.builder()
                    .username(credentials.getUsername())
                    .build();
        }
        return null;
    }

    @ModelAttribute("role")
    public String getRole() {
        String role = "";
        Credentials credentials = this.getCredentials();
        if (credentials != null) {
            role = credentials.getRole()
                    .name();
        }
        return role;
    }



    private Credentials getCredentials() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Object user = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            if (user instanceof UserDetails userDetails) {
                return credentialsService.getCredentials(userDetails.getUsername());
            } else if (user instanceof CustomOAuth2User loggedOAuth2User) {
                String username = loggedOAuth2User.getLogin();
                return credentialsService.getCredentials(username);
            }
        }
        return null;
    }



}
