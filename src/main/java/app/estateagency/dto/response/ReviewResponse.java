package app.estateagency.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private Long agentID;
    private String agent;
    private Long reviewerID;
    private String reviewer;
    private Long archivedOfferID;
    private int rating;
    private String comment;
    private LocalDateTime postDate;
}
