package app.estateagency.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be max 10")
    private Integer rating;

    @NotNull(message = "Comment cannot be null")
    @NotBlank(message = "Comment cannot be blank")
    private String comment;
}
