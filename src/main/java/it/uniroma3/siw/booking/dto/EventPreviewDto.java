package it.uniroma3.siw.booking.dto;


import it.uniroma3.siw.booking.model.Event;
import lombok.Data;

@Data
public class EventPreviewDto {

    private Long id;
    private String title;
    private String imageSrc;
    private Double vote;

    public EventPreviewDto(Event event) {
        //TODO
    }
}
