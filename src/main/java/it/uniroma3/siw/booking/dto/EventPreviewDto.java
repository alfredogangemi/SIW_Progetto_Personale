package it.uniroma3.siw.booking.dto;


import it.uniroma3.siw.booking.model.Event;
import lombok.Data;

@Data
public class EventPreviewDto {

    private Long id;
    private String name;
    private String imageSrc;
    private String description;

    public EventPreviewDto(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        if (this.description.length() > 80) {
            this.description = description.substring(0, 73) + "...";
        }
        if (event.getImage() != null) {
            this.imageSrc = event.getImage()
                    .generateHtmlSource();
        }
    }
}
