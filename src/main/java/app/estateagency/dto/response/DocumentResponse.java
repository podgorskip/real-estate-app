package app.estateagency.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentResponse {
    private Long documentID;
    private Long estateID;
    private String filename;
    private LocalDateTime uploadDate;
}
