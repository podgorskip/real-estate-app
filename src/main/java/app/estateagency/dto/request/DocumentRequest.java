package app.estateagency.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class DocumentRequest {
    @NotNull(message = "Estate ID cannot be null")
    @Positive(message = "Estate ID must be positive")
    private Long estateID;

    @NotBlank(message = "Description cannot be blank")
    private String description;
}
