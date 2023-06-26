package it.uniroma3.siw.booking.service;

import it.uniroma3.siw.booking.dto.EventPreviewDto;
import it.uniroma3.siw.booking.model.Event;
import it.uniroma3.siw.booking.model.Image;
import it.uniroma3.siw.booking.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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
    public void save(Event event) {
        this.eventRepository.save(event);
    }

    @Transactional
    public void save(Event event, MultipartFile file) throws IOException {
        if (file != null) {
            Image image = Image.builder()
                    .name(file.getOriginalFilename())
                    .content(file.getBytes())
                    .type(file.getContentType())
                    .build();
            event.setImage(image);
        }
        eventRepository.save(event);
    }

    @Transactional
    public Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public List<Event> findAllByDateAfter() {
        return eventRepository.findAllByDateAfterOrDateEquals();
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return eventRepository.existsById(id);
    }

    @Transactional
    public void saveWithPresentImage(Event event) {
        Event persistedEvent = eventRepository.findById(event.getId())
                .get();
        Image oldImage = persistedEvent.getImage();
        event.setImage(oldImage);
        eventRepository.save(event);
    }


    @Transactional
    public List<EventPreviewDto> getIncomingEventsPreviews() {
        List<EventPreviewDto> latestEvents = new LinkedList<>();
        eventRepository.findIncomingEvents()
                .forEach(event -> latestEvents.add(new EventPreviewDto(event)));
        return latestEvents;
    }

    public Iterable<Event> findAll() {
        return eventRepository.findAll();
    }
}
