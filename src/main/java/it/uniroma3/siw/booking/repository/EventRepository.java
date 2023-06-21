package it.uniroma3.siw.booking.repository;

import it.uniroma3.siw.booking.model.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    @Query(value = "SELECT * FROM event WHERE date >= CURRENT_DATE ORDER BY date", nativeQuery = true)
    List<Event> findAllByDateAfterOrDateEquals();

    @Query(value = "SELECT * FROM event WHERE date >= CURRENT_DATE ORDER BY date limit 3", nativeQuery = true)
    List<Event> findIncomingEvents();
}
