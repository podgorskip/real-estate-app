package app.estateagency.dto.response;

import app.estateagency.enums.estate.Availability;
import lombok.Data;

@Data
public class OfferPreviewResponse {
    private Long id;
    private Long estateID;
    private String location;
    private Double price;
    private String description;
    private Availability availability;
}
