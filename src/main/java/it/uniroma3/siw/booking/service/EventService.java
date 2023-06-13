package it.uniroma3.siw.booking.service;

import it.uniroma3.siw.booking.model.Event;
import it.uniroma3.siw.booking.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class EventService {

    protected EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Transactional
    public Event getEvent(Long id) {
        Optional<Event> result = this.eventRepository.findById(id);
        return result.orElse(null);
    }


    @Transactional
    public void saveEvent(Event event) {
        this.eventRepository.save(event);
    }



}
