package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "agent", schema = "real_estate")
@Data
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "agent")
    private Set<Calendar> calendars;

    @OneToMany(mappedBy = "agent")
    private Set<Estate> estates;

    @OneToMany(mappedBy = "agent")
    private Set<Meeting> meetings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(id, agent.id) && Objects.equals(user, agent.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
