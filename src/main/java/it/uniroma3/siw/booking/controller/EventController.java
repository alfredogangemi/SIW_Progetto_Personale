package it.uniroma3.siw.booking.controller;

import it.uniroma3.siw.booking.constants.Role;
import it.uniroma3.siw.booking.controller.validator.EventValidator;
import it.uniroma3.siw.booking.controller.validator.ImageValidator;
import it.uniroma3.siw.booking.model.Event;
import it.uniroma3.siw.booking.model.Image;
import it.uniroma3.siw.booking.service.EventService;
import it.uniroma3.siw.booking.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@Controller
@Slf4j
public class EventController {

    protected final EventService eventService;
    protected final ReservationService reservationService;
    protected final ImageValidator imageValidator;
    protected final EventValidator eventValidator;
    protected final GlobalController globalController;

    @Autowired
    public EventController(EventService eventService, ReservationService reservationService, ImageValidator imageValidator,
            EventValidator eventValidator, GlobalController globalController) {
        this.eventService = eventService;
        this.reservationService = reservationService;
        this.imageValidator = imageValidator;
        this.eventValidator = eventValidator;
        this.globalController = globalController;
    }


    @GetMapping(value = "/admin/addEvent")
    public String formNewArtist(Model model) {
        model.addAttribute("event", new Event());
        return "admin/addEvent";
    }

    @PostMapping("/admin/createEvent")
    public String createNewArtist(@Validated @ModelAttribute("event") Event event, @RequestParam("imageFile") MultipartFile file, Model model,
            BindingResult bindingResult) {
        eventValidator.validate(event, bindingResult);
        if (file != null && !file.isEmpty()) {
            imageValidator.validate(file, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return "admin/addEvent";
        }
        try {
            if (file != null && !file.isEmpty()) {
                eventService.save(event, file);
            } else {
                eventService.save(event);
            }
        } catch (IOException ioex) {
            log.error("Errore nella gestione dell'allegato nell'evento", ioex);
            bindingResult.reject("image.upload.generic.error");
            return "admin/addEvent";
        } catch (Exception ex) {
            log.error("Errore generico durante la creazione dell'evento", ex);
            bindingResult.reject("event.generic.error");
            return "admin/addEvent";
        }
        model.addAttribute("event", event);
        return "redirect:/event/" + event.getId();
    }


    @GetMapping("/event/{id}")
    public String getEvent(@PathVariable("id") Long id, Model model) {
        Event event = eventService.findEventById(id);
        if (event != null) {
            model.addAttribute("event", event);
        }
        return "event";
    }

    @GetMapping("/searchEvents")
    public String events(Model model) {
        String role = globalController.getRole();
        Iterable<Event> events;
        if (Role.ADMIN.name()
                .equals(role)) {
            events = eventService.findAll(); //admin visualizza anche gli eventi passati
        } else {
            events = eventService.findAllByDateAfter();
        }
        model.addAttribute("events", events);
        return "events";
    }

    @PostMapping("/admin/event/delete")
    public String delete(@ModelAttribute("id") Long id, RedirectAttributes redirectAttributes) {
        if (id != null && eventService.existsById(id)) {
            Event event = eventService.findEventById(id);
            if (reservationService.existByEvent(event)) {
                log.warn("Impossibile eliminare l'evento {} --- (#{}) perchè sono presenti delle prenotazioni", event.getName(), event.getId());
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Impossibile eliminare questo evento in quanto sono presenti una o più prenotazioni.");
                return "redirect:/event/" + id;
            }
            eventService.deleteById(id);
        } else {
            log.warn("Errore durante l'emininazione dell'evento con id {}", id);
        }
        return "redirect:/";
    }


    @PostMapping("/admin/updateEvent")
    public String updateEvent(@Validated @ModelAttribute("event") Event event, @RequestParam("imageFile") MultipartFile file, Model model,
            BindingResult bindingResult, @ModelAttribute("id") Long id) {
        if (id != null && !eventService.existsById(id)) {
            bindingResult.reject("artist.generic.error");
            return "admin/updateEvent";
        }
        eventValidator.validate(event, bindingResult);
        boolean isNewImageUpdated = file != null && !file.isEmpty();
        if (isNewImageUpdated) {
            imageValidator.validate(file, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return "admin/updateEvent";
        }
        try {
            event.setId(id);
            if (isNewImageUpdated) {
                eventService.save(event, file);
            } else {
                eventService.saveWithPresentImage(event);
            }
        } catch (IOException ioex) {
            log.error("Errore nella gestione dell'allegato nell'evento", ioex);
            bindingResult.reject("image.upload.generic.error");
            return "admin/updateEvent";
        } catch (Exception ex) {
            log.error("Errore generico durante l'aggiornamento dell'artista");
            ex.printStackTrace();
            bindingResult.reject("event.generic.error");
            return "admin/updateEvent";
        }
        //        model.addAttribute("event", event);
        return "redirect:/event/" + event.getId();
    }


    @GetMapping("/admin/event/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        log.info("Redirecting to edit form for event with ID {}", id);
        Event event = eventService.findEventById(id);
        model.addAttribute("event", event);
        return "admin/updateEvent";
    }



    @GetMapping("/removeImageFromEvent/{eventId}")
    public String removeImage(@PathVariable("eventId") Long id, Model model) {
        Event event = eventService.findEventById(id);
        if (event != null) {
            event.setImage(new Image());
            eventService.save(event);
        }
        return showEditForm(id, model);
    }



}
