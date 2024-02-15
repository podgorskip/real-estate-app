package app.estateagency.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CalendarRequest {
    @NotNull(message = "Slots cannot be null")
    private Set<@Future(message = "Slots should be in the future") LocalDateTime> slots;
}
