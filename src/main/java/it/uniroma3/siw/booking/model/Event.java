package it.uniroma3.siw.booking.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd") //formato utile per thymeleaf
    private LocalDate date;

    @OneToOne(cascade = {CascadeType.ALL})
    private Image image;

    private Integer numberOfPartecipants;

    private Double price;

    //TODO -> Set di prenotazioni



    //TODO -> Equals e hashcode

}
