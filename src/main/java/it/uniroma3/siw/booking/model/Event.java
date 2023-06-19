package it.uniroma3.siw.booking.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") //formato utile per thymeleaf
    private LocalDateTime date;

    @OneToOne(cascade = {CascadeType.ALL})
    private Image image;

    private Integer numberOfPartecipants;

    private Double price;


    //TODO -> Set di prenotazioni



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(date, event.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date);
    }
}
