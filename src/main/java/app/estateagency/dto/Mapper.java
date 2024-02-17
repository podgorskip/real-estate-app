package app.estateagency.dto;

import app.estateagency.dto.response.*;
import app.estateagency.jpa.entities.*;
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

    @Mapping(source = "meeting.user.fullName", target = "user")
    @Mapping(source = "meeting.agent.user.fullName", target = "agent")
    MeetingResponse convertMeetings(Meeting meeting);

    @Mapping(source = "offer.estate.agent.id", target = "agentID")
    @Mapping(source = "offer.estate.agent.user.fullName", target = "agent")
    @Mapping(source = "offer.estate.type", target = "type")
    @Mapping(source = "offer.estate.bathrooms", target = "bathrooms")
    @Mapping(source = "offer.estate.rooms", target = "rooms")
    @Mapping(source = "offer.estate.garage", target = "garage")
    @Mapping(source = "offer.estate.storey", target = "storey")
    @Mapping(source = "offer.estate.location", target = "location")
    @Mapping(source = "offer.estate.balcony", target = "balcony")
    @Mapping(source = "offer.estate.availability", target = "availability")
    @Mapping(source = "offer.estate.size", target = "size")
    @Mapping(source = "offer.estate.condition", target = "condition")
    OfferResponse convertOffer(Offer offer);

    @Mapping(source = "offer.estate.location", target = "location")
    @Mapping(source = "offer.estate.availability", target = "availability")
    OfferPreviewResponse convertOfferPreview(Offer offer);

    @Mapping(source = "archivedOffer.estate.agent.id", target = "agentID")
    @Mapping(source = "archivedOffer.estate.agent.user.fullName", target = "agent")
    @Mapping(source = "archivedOffer.estate.owner.user.id", target = "ownerID")
    @Mapping(source = "archivedOffer.estate.owner.user.fullName", target = "owner")
    @Mapping(source = "archivedOffer.customer.user.id", target = "customerID")
    @Mapping(source = "archivedOffer.customer.user.fullName", target = "customer")
    @Mapping(source = "archivedOffer.estate.type", target = "type")
    @Mapping(source = "archivedOffer.estate.bathrooms", target = "bathrooms")
    @Mapping(source = "archivedOffer.estate.rooms", target = "rooms")
    @Mapping(source = "archivedOffer.estate.garage", target = "garage")
    @Mapping(source = "archivedOffer.estate.storey", target = "storey")
    @Mapping(source = "archivedOffer.estate.location", target = "location")
    @Mapping(source = "archivedOffer.estate.balcony", target = "balcony")
    @Mapping(source = "archivedOffer.estate.availability", target = "availability")
    @Mapping(source = "archivedOffer.estate.size", target = "size")
    @Mapping(source = "archivedOffer.estate.condition", target = "condition")
    ArchivedOfferResponse convertArchivedOffer(ArchivedOffer archivedOffer);

    @Mapping(source = "review.archivedOffer.estate.agent.id", target = "agentID")
    @Mapping(source = "review.archivedOffer.estate.agent.user.fullName", target = "agent")
    @Mapping(source = "review.user.id", target = "reviewerID")
    @Mapping(source = "review.user.fullName", target = "reviewer")
    @Mapping(source = "review.archivedOffer.id", target = "archivedOfferID")
    ReviewResponse convertReview(Review review);
}
