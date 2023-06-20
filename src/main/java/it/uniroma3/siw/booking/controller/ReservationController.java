package it.uniroma3.siw.booking.controller;


import it.uniroma3.siw.booking.model.Event;
import it.uniroma3.siw.booking.model.Reservation;
import it.uniroma3.siw.booking.model.User;
import it.uniroma3.siw.booking.service.EventService;
import it.uniroma3.siw.booking.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class ReservationController {


    private final EventService eventService;
    private final GlobalController globalController;
    private final ReservationService reservationService;


    @Autowired
    public ReservationController(ReservationService reservationService, EventService eventService, GlobalController globalController) {
        this.reservationService = reservationService;
        this.eventService = eventService;
        this.globalController = globalController;
    }



    @GetMapping("/subscribe/{eventId}")
    public String subscribe(@PathVariable("eventId") Long id, RedirectAttributes redirectAttributes) {
        Event event = eventService.findEventById(id);
        User user = globalController.getCurrentUser();
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
        User user = globalController.getCurrentUser();
        model.addAttribute("reservations", reservationService.findIncomingReservationsByUser(user));
        model.addAttribute("pastReservations", reservationService.findOldReservationsByUser(user));
        return "myReservations";
    }



}
