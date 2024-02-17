package app.estateagency.dto.response;

import app.estateagency.enums.estate.Availability;
import app.estateagency.enums.estate.Condition;
import app.estateagency.enums.estate.EstateType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArchivedOfferResponse {
    private Long id;
    private Long agentID;
    private String agent;
    private Long ownerID;
    private String owner;
    private Long customerID;
    private String customer;
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
    private Double price;
    private LocalDateTime archiveDate;
}
