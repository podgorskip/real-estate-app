package app.estateagency.services;

import app.estateagency.dto.request.EstateRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.jpa.EstateFilter;
import app.estateagency.jpa.entities.Agent;
import app.estateagency.jpa.entities.Estate;
import app.estateagency.jpa.entities.Owner;
import app.estateagency.jpa.repositories.EstateRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * A service allowing to handle business logic related to Estate entities
 */
@Service
@RequiredArgsConstructor
public class EstateService {
    private final EstateRepository estateRepository;
    private final AgentService agentService;
    private final OwnerService ownerService;

    /**
     * Allows owners to report an estate to be posted on the service
     * @param username Username of the owner reporting the estate
     * @param estateRequest Details of the reported estate
     * @return Response containing info if successfully reported the offer
     */
    @Transactional
    public Response reportEstate(String username, EstateRequest estateRequest) {
        Optional<Owner> owner = ownerService.getByUsername(username);
        Optional<Agent> agent = agentService.getByID(estateRequest.getAgent());

        if (owner.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No account of the provided username found");

        if (agent.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No agent of the provided username found");

        Estate estate = createEstate(owner.get(), agent.get(), estateRequest);

        estateRepository.save(estate);

        return new Response(true, HttpStatus.CREATED, "Successfully reported the offer");
    }

    /**
     * Retrieves all the reported estates which have a document and photos attached
     * @param username Username of agent who retrieves reported estates
     * @return List of estates if present, empty otherwise
     */
    public Optional<List<Estate>> getReportedEstatesByAgentUsername(String username) {
        Optional<Agent> agent = agentService.getByUsername(username);

        if (agent.isEmpty())
            return Optional.empty();

        return estateRepository.findReportedEstatesByAgent(agent.get());
    }

    /**
     * Retrieves all the estates
     * @return List of estates if present, empty otherwise
     */
    public Optional<List<Estate>> getAllEstates() {
        List<Estate> estates = estateRepository.findAll();

        if (estates.isEmpty())
            return Optional.empty();

        return Optional.of(estates);
    }

    /**
     * Retrieves an estate by its ID
     * @param id ID of the estate
     * @return Estate is present, empty otherwise
     */
    public Optional<Estate> getByID(Long id) {
        return estateRepository.findById(id);
    }

    /**
     * Retrieves estates based on the predicates passed as arguments
     * @return List of estates if predicates match any, empty otherwise
     */
    public Optional<List<Estate>> getFilteredEstates(Integer bathrooms, Integer rooms, Boolean garage, Integer storey,
                                                     String location, Boolean balcony, Double size, String condition,
                                                     String type, String availability, Double priceFrom, Double priceTo,
                                                     LocalDateTime postFrom, LocalDateTime postTo) {

        return estateRepository.findAll(EstateFilter.filterEstates(type, bathrooms, rooms, garage, storey, location, balcony,
                                        availability, size, condition, priceFrom, priceTo, postFrom, postTo));
    }

    /**
     * Allows to create a fully populated Estate entity
     * @param owner Owner of the estate
     * @param agent Agent assigned to the estate
     * @param estateRequest Details of the estate
     * @return Fully populated Estate entity
     */
    private Estate createEstate(Owner owner, Agent agent, EstateRequest estateRequest) {
        Estate estate = new Estate();

        estate.setOwner(owner);
        estate.setAgent(agent);
        estate.setType(estateRequest.getType());
        estate.setBathrooms(estateRequest.getBathrooms());
        estate.setRooms(estateRequest.getRooms());
        estate.setGarage(estateRequest.getGarage());
        estate.setStorey(estateRequest.getStorey());
        estate.setLocation(estateRequest.getLocation());
        estate.setBalcony(estateRequest.getBalcony());
        estate.setDescription(estateRequest.getDescription());
        estate.setAvailability(estateRequest.getAvailability());
        estate.setSize(estateRequest.getSize());
        estate.setCondition(estateRequest.getCondition());
        estate.setOfferedPrice(estateRequest.getOfferedPrice());

        return estate;
    }
}
