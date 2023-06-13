package it.uniroma3.siw.booking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Reservation {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    private String note;

    //TODO -> Equals e hashcode
}
