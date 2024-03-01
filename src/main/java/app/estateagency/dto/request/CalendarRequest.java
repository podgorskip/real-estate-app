package app.estateagency.dto.request;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CalendarRequest {
    @NotNull(message = "Slots cannot be null")
    private Set<@Future(message = "Slots should be in the future") LocalDateTime> slots;
}
