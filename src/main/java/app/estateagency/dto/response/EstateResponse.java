package app.estateagency.dto.response;

import app.estateagency.enums.estate.EstateType;
import app.estateagency.enums.estate.Availability;
import app.estateagency.enums.estate.Condition;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EstateResponse {
    private Long agentID;
    private String agent;
    private Long ownerID;
    private String owner;
    private EstateType type;
    private Integer bathrooms;
    private Integer rooms;
    private Boolean garage;
    private Integer storey;
    private String location;
    private Boolean balcony;
    private String description;
    private Availability availability;
    private Double size;
    private Condition condition;
    private Double offeredPrice;
    private LocalDateTime postDate;
}
