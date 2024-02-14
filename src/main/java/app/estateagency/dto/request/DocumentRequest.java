package app.estateagency.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DocumentRequest {
    @NotNull(message = "Estate ID cannot be null")
    @Positive(message = "Estate ID must be positive")
    private Long estateID;

    @NotBlank(message = "Description cannot be blank")
    private String description;
}
