package app.estateagency.enums;


import java.util.Set;
import static app.estateagency.enums.Privilege.*;

public enum Role {
    CUSTOMER (Set.of(CHANGE_PASSWORD, CHANGE_CREDENTIALS, CHECK_OFFERS, CHECK_CALENDAR, CHECK_AGENTS, SCHEDULE_MEETING)),
    OWNER(Set.of(CHANGE_PASSWORD, CHANGE_CREDENTIALS, REPORT_OFFER, CHECK_CALENDAR, CHECK_AGENTS, ADD_DOCUMENTS, UPLOAD_PHOTO, SCHEDULE_MEETING)),
    ADMIN(Set.of(CHANGE_PASSWORD, CHANGE_CREDENTIALS, ADD_AGENT, REMOVE_USER, ADD_ADMIN, CHECK_AGENTS)),
    AGENT (Set.of(CHANGE_PASSWORD, CHANGE_CREDENTIALS, ADD_OFFER, ADD_MEETINGS,  CHECK_AGENTS, CHECK_REPORTED_ESTATES, CHECK_DOCUMENTS, CHECK_PHOTOS));
    private final Set<Privilege> privileges;
    Role(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Set<Privilege> privileges() {
        return privileges;
    }

}
