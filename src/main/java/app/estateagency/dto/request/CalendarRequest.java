package app.estateagency.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CalendarRequest {
    @NotNull
    private Set<LocalDateTime> slots;
}
