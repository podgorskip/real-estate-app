package app.estateagency.dto;

import app.estateagency.dto.response.AgentResponse;
import app.estateagency.dto.response.CalendarResponse;
import app.estateagency.dto.response.DocumentResponse;
import app.estateagency.dto.response.EstateResponse;
import app.estateagency.jpa.entities.Agent;
import app.estateagency.jpa.entities.Calendar;
import app.estateagency.jpa.entities.Document;
import app.estateagency.jpa.entities.Estate;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {
    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    @Mapping(source = "estate.agent.id", target = "agentID")
    @Mapping(source = "estate.agent.user.fullName", target = "agent")
    @Mapping(source = "estate.owner.id", target = "ownerID")
    @Mapping(source = "estate.owner.user.fullName", target = "owner")
    EstateResponse convertEstate(Estate estate);

    @Mapping(source = "agent.user.fullName", target = "fullName")
    @Mapping(source = "agent.user.email", target = "email")
    @Mapping(source = "agent.user.phoneNumber", target = "phoneNumber")
    AgentResponse convertAgent(Agent agent);

    @Mapping(source = "document.estate.id", target = "estateID")
    DocumentResponse convertDocument(Document document);

    @Mapping(source = "calendar.agent.id", target = "agentID")
    CalendarResponse convertCalendar(Calendar calendar);
}
