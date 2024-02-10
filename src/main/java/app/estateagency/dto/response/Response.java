package app.estateagency.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {
    private boolean success;
    private HttpStatus status;
    private String response;
}
