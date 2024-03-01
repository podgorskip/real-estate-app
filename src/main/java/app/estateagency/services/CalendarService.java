package app.estateagency.services;

import app.estateagency.dto.request.CalendarRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.jpa.entities.Agent;
import app.estateagency.jpa.entities.Calendar;
import app.estateagency.jpa.repositories.CalendarRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final AgentService agentService;

    /**
     * Allows agents to add available meeting slots
     * @param username Username of the agent for whom slots are added
     * @param calendarRequest List of available dates
     * @return Response if successfully added meeting slots
     */
    @Transactional
    public Response addAvailableMeetings(String username, CalendarRequest calendarRequest) {
        Optional<Agent> optionalAgent = agentService.getByUsername(username);

        if (optionalAgent.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No agent of the provided username found");

        Agent agent = optionalAgent.get();

        calendarRequest.getSlots().forEach(slot -> {
            Calendar calendar = new Calendar();
            calendar.setAgent(agent);
            calendar.setDate(slot);
            calendarRepository.save(calendar);
        });

        return new Response(true, HttpStatus.CREATED, "Successfully added available slots");
    }

    /**
     * Retrieves the agent's calendar
     * @param id ID of the agent to have the calendar checked
     * @return List of available slots if present
     */
    public Optional<List<Calendar>> checkAgentCalendar(Long id) {
        Optional<Agent> agent = agentService.getByID(id);

        if (agent.isEmpty())
            return Optional.empty();

        return calendarRepository.findByAgent(agent.get());
    }

    /**
     * Retrieves calendar by its ID
     * @param id ID of the calendar
     * @return Calendar object if present, empty otherwise
     */
    public Optional<Calendar> getByID(Long id) {
        return calendarRepository.findById(id);
    }

    /**
     * Allows to delete a Calendar object
     * @param calendar Calendar object
     */
    public void deleteCalendar(Calendar calendar) {
        calendarRepository.delete(calendar);
    }
}
