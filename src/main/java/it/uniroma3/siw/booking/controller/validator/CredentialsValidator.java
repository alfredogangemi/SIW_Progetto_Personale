package it.uniroma3.siw.booking.controller.validator;

import io.micrometer.common.util.StringUtils;
import it.uniroma3.siw.booking.model.Credentials;
import it.uniroma3.siw.booking.service.CredentialsService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class CredentialsValidator implements Validator {

    protected CredentialsService credentialsService;

    @Autowired
    public CredentialsValidator(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        Credentials credentials = (Credentials) o;
        if (StringUtils.isBlank(credentials.getUsername())) {
            log.warn("Empty username field in user registration");
            errors.reject("user.empty.username");
            return;
        }
        if (credentials.getUsername()
                .length() < 5) {
            log.warn("Username {} is too short in user registration", credentials.getUsername());
            errors.reject("user.username.too.short");
        }
        if (credentialsService.existsByUsername(credentials.getUsername())) {
            log.warn("Username {} already exists in user registration", credentials.getUsername());
            errors.reject("user.username.already.present");
        }
        if (StringUtils.isBlank(credentials.getPassword())) {
            log.warn("Empty password field in user registration");
            errors.reject("user.empty.password");
        }
        if (credentials.getPassword()
                .length() < 8) {
            log.warn("Password is too short in user registration");
            errors.reject("user.password.too.short");
        }
    }


    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return Credentials.class.equals(aClass);
    }


}
