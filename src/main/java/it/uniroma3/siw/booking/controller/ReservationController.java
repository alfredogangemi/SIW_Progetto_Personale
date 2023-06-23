package it.uniroma3.siw.booking.controller;


import it.uniroma3.siw.booking.model.Event;
import it.uniroma3.siw.booking.model.Reservation;
import it.uniroma3.siw.booking.model.User;
import it.uniroma3.siw.booking.service.EventService;
import it.uniroma3.siw.booking.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class ReservationController {

    protected final HttpServletRequest request;
    protected final EventService eventService;
    protected final AuthenticationController authenticationController;
    protected final ReservationService reservationService;


    @Autowired
    public ReservationController(HttpServletRequest request, ReservationService reservationService, EventService eventService,
            AuthenticationController authenticationController) {
        this.request = request;
        this.reservationService = reservationService;
        this.eventService = eventService;
        this.authenticationController = authenticationController;
    }



    @GetMapping("/subscribe/{eventId}")
    public String subscribe(@PathVariable("eventId") Long id, RedirectAttributes redirectAttributes) {
        Event event = eventService.findEventById(id);
        User user = authenticationController.getCurrentUser();
        if (reservationService.existByUserAndEvent(user, event)) {
            log.info("Prenotazione all'evento {} già presente per l'utente {}", event.getName(), event.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Hai già effettuato una prenotazione per questo evento.");
            return "redirect:/event/" + id;
        }
        Integer reservationCount = reservationService.countByEvent(event);
        if (reservationCount >= event.getNumberOfPartecipants()) {
            log.info("Posti esauriti per l'evento {} -- ({})", event.getName(), event.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Questo evento è sold-out.");
            return "redirect:/event/" + id;
        }
        Reservation reservation = new Reservation();
        reservation.setEvent(event);
        reservation.setCreationDate(LocalDateTime.now());
        reservation.setUser(user);
        reservationService.saveReservation(reservation);
        return "subscribedSuccessfully";
    }


    @GetMapping("/myReservations")
    public String myReservations(Model model) {
        User user = authenticationController.getCurrentUser();
        model.addAttribute("reservations", reservationService.findIncomingReservationsByUser(user));
        model.addAttribute("pastReservations", reservationService.findOldReservationsByUser(user));
        return "myReservations";
    }


    @GetMapping("/admin/manageReservations/{eventId}")
    public String myReservations(@PathVariable("eventId") Long id, Model model) {
        Event event = eventService.findEventById(id);
        model.addAttribute("reservations", reservationService.findReservationsByEvent(event));
        return "admin/manageReservations";
    }


    @PostMapping("/unsubscribe")
    public String delete(@ModelAttribute("reservationId") Long id) {
        if (id != null && reservationService.existsById(id)) {
            reservationService.deleteById(id);
        } else {
            log.warn("Errore durante l'emininazione della prenotazione con id {}", id);
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }



}
