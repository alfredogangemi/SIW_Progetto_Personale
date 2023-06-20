package it.uniroma3.siw.booking.service;

import it.uniroma3.siw.booking.model.Event;
import it.uniroma3.siw.booking.model.Reservation;
import it.uniroma3.siw.booking.model.User;
import it.uniroma3.siw.booking.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    protected ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation getReservation(Long id) {
        Optional<Reservation> result = this.reservationRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public void saveReservation(Reservation reservation) {
        this.reservationRepository.save(reservation);
    }

    @Transactional
    public boolean existByUserAndEvent(User user, Event event) {
        return reservationRepository.existsByUserAndEvent(user, event);
    }

    @Transactional
    public Integer countByEvent(Event event) {
        return reservationRepository.countByEvent(event);
    }


    @Transactional
    public List<Reservation> findIncomingReservationsByUser(User user) {
        return reservationRepository.findReservationsByUserAndEventDateAfterOrEqual(user, LocalDateTime.now());
    }

    @Transactional
    public List<Reservation> findOldReservationsByUser(User user) {
        return reservationRepository.findReservationsByUserAndEventDateBefore(user, LocalDateTime.now());
    }
}
