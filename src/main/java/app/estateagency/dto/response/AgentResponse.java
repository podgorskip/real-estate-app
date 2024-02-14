package app.estateagency.dto.response;

import lombok.Data;

@Data
public class AgentResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
}
