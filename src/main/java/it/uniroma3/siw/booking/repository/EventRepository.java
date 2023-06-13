package it.uniroma3.siw.booking.repository;

import it.uniroma3.siw.booking.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {

}
