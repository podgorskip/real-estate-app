package app.estateagency.jpa.entities;

import app.estateagency.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "meeting", schema = "real_estate")
@Data
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    private LocalDateTime date;

    @Enumerated(value = EnumType.STRING)
    private Role role;
}
