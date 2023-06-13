package it.uniroma3.siw.booking.repository;

import it.uniroma3.siw.booking.model.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

}
