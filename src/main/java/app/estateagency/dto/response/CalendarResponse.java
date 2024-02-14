package app.estateagency.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CalendarResponse {
    private Long id;
    private Long agentID;
    private LocalDateTime date;
}
