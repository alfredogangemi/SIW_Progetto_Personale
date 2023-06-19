package it.uniroma3.siw.booking.controller.validator;

import it.uniroma3.siw.booking.model.Event;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
@Slf4j
public class EventValidator implements Validator {


    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Event event = (Event) target;
        log.debug("Starting validate new event...");

        if (event.getName() == null || event.getName()
                .trim()
                .isEmpty()) {
            errors.reject("event.name.required");
        }

        if (event.getDescription() == null || event.getDescription()
                .trim()
                .isEmpty()) {
            errors.reject("event.description.required");
        }

        // Check date
        if (event.getDate() == null || event.getDate()
                .isBefore(LocalDateTime.now())) {
            errors.reject("event.date.invalid");
        }

        if (event.getNumberOfPartecipants() == null) {
            errors.rejectValue("numberOfParticipants", "event.participants.required");
        } else if (event.getNumberOfPartecipants() <= 0) {
            errors.reject("event.participants.invalid");
        }

        if (event.getPrice() != null && event.getPrice() < 0) {
            errors.reject("event.price.invalid");
        }
    }
}
