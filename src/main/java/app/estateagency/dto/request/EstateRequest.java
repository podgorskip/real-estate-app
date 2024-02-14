package app.estateagency.dto.request;

import app.estateagency.enums.estate.EstateType;
import app.estateagency.enums.estate.Availability;
import app.estateagency.enums.estate.Condition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EstateRequest {
    @NotNull(message = "Agent ID cannot be null")
    @Positive(message = "Agent ID must be positive")
    private Long agent;

    @NotNull(message = "Estate type cannot be null")
    private EstateType type;

    @NotNull(message = "Bathrooms number cannot be null")
    @Positive(message = "Bathroom number must be positive")
    private Integer bathrooms;

    @NotNull(message = "Rooms number cannot be null")
    @Positive(message = "Rooms number must be positive")
    private Integer rooms;

    @NotNull(message = "Garage info cannot be null")
    private Boolean garage;

    @NotNull(message = "Storey number cannot be null")
    @Positive(message = "Storey number must be positive")
    private Integer storey;

    @NotNull(message = "Location cannot be null")
    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotNull(message = "Balcony info cannot be null")
    private Boolean balcony;

    @NotBlank(message = "Description cannot be null")
    @Size(min = 20, message = "Description must contain at least 20 characters")
    private String description;

    @NotNull(message = "Availability cannot be null")
    private Availability availability;

    @NotNull(message = "Size cannot be null")
    @Positive(message = "Size must be positive")
    private Double size;

    @NotNull(message = "Condition cannot be null")
    private Condition condition;

    @NotNull(message = "Offered price cannot be null")
    @Positive(message = "Offered price must be positive")
    private Double offeredPrice;
}
