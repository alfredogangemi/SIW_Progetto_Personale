package it.uniroma3.siw.booking.controller;

import it.uniroma3.siw.booking.model.Event;
import it.uniroma3.siw.booking.service.EventService;
import it.uniroma3.siw.booking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiwBookingRestController {

    protected final EventService eventService;
    protected final ReservationService reservationService;

    @Autowired
    public SiwBookingRestController(EventService eventService, ReservationService reservationService) {
        this.eventService = eventService;
        this.reservationService = reservationService;
    }


    @GetMapping("/rest/event/{id}")
    public Event getEvent(@PathVariable("id") Long id) {
        return eventService.findEventById(id);
    }

    @GetMapping("/rest/events")
    public Iterable<Event> getAllEvents() {
        return eventService.findAll();
    }


    @GetMapping("/rest/incomingEvents")
    public Iterable<Event> getIncomingEvents() {
        return eventService.findAllByDateAfter();
    }

}
