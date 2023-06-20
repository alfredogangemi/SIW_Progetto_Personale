package it.uniroma3.siw.booking.repository;

import it.uniroma3.siw.booking.model.Event;
import it.uniroma3.siw.booking.model.Reservation;
import it.uniroma3.siw.booking.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    boolean existsByUserAndEvent(User user, Event event);

    Integer countByEvent(Event event);

    List<Reservation> findAllByUser(User user);

    @Query("SELECT r FROM Reservation r WHERE r.user = :user AND r.event.date >= :currentDate")
    List<Reservation> findReservationsByUserAndEventDateAfterOrEqual(@Param("user") User user, @Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT r FROM Reservation r WHERE r.user = :user AND r.event.date < :currentDate")
    List<Reservation> findReservationsByUserAndEventDateBefore(@Param("user") User user, @Param("currentDate") LocalDateTime currentDate);
}
