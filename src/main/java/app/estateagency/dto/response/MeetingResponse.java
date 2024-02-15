package app.estateagency.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeetingResponse {
    private Long id;
    private String agent;
    private String user;
    private String role;
    private LocalDateTime date;
}
