package it.uniroma3.siw.booking.repository;

import it.uniroma3.siw.booking.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {


    boolean existsByEmail(String email);
}
