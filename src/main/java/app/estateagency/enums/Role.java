package app.estateagency.enums;


import java.util.Set;
import static app.estateagency.enums.Privilege.*;

public enum Role {
    CUSTOMER (Set.of(CHANGE_PASSWORD, CHANGE_CREDENTIALS, CHECK_OFFERS, CHECK_CALENDAR)),
    OWNER(Set.of(CHANGE_PASSWORD, CHANGE_CREDENTIALS, REPORT_OFFER, CHECK_CALENDAR)),
    ADMIN(Set.of(CHANGE_PASSWORD, CHANGE_CREDENTIALS, ADD_AGENT, REMOVE_USER)),
    AGENT (Set.of(CHANGE_PASSWORD, CHANGE_CREDENTIALS, ADD_OFFER, ADD_MEETINGS));
    private final Set<Privilege> privileges;
    Role(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Set<Privilege> privileges() {
        return privileges;
    }

}
