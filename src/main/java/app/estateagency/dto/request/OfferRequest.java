package app.estateagency.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OfferRequest {
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 20, message = "Description must be at least 20 characters long")
    private String description;
}
