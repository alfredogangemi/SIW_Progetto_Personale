package it.uniroma3.siw.booking.controller.validator;

import io.micrometer.common.util.StringUtils;
import it.uniroma3.siw.booking.model.User;
import it.uniroma3.siw.booking.service.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class UserValidator implements Validator {

    protected UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;
        log.debug("Starting user validation...");
        if (StringUtils.isBlank(user.getName())) {
            log.debug("User name is empty");
            errors.reject("user.empty.name");
            return;
        }
        if (StringUtils.isBlank(user.getSurname())) {
            log.debug("User surname is empty");
            errors.reject("user.empty.surname");
            return;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            log.debug("User email is empty");
            errors.reject("user.empty.email");
        } else if (!EmailValidator.getInstance()
                .isValid(user.getEmail())) {
            log.debug("User email is not valid");
            errors.reject("user.not.valid.email");
        } else if (userService.existsByEmail(user.getEmail())) {
            log.debug("User with this email already exists");
            errors.reject("user.with.this.email.already.present");
        }
    }


    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return User.class.equals(aClass);
    }


}
