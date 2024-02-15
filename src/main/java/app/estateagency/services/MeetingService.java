package app.estateagency.services;

import app.estateagency.dto.response.Response;
import app.estateagency.enums.Role;
import app.estateagency.jpa.entities.*;
import app.estateagency.jpa.repositories.MeetingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * A service allowing to handle business logic related to meetings scheduling
 */
@Service
@RequiredArgsConstructor
public class MeetingService {
    private final CalendarService calendarService;
    private final MeetingRepository meetingRepository;
    private final UserService userService;

    /**
     * Allows owners and customers to schedule meetings with agents
     * @param username Username of the owner/customer who schedules the meeting
     * @param id ID of the available calendar
     * @return Response if successfully scheduled meeting
     */
    @Transactional
    public Response scheduleMeeting(String username, Long id) {
        Optional<User> user = userService.getByUsername(username);

        if (user.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No account of the provided username found");

        Optional<Calendar> calendar = calendarService.getByID(id);

        if (calendar.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No available meeting found");

        Meeting meeting = new Meeting();
        meeting.setUser(user.get());
        meeting.setAgent(calendar.get().getAgent());
        meeting.setDate(calendar.get().getDate());
        meeting.setRole(user.get().getRole());

        meetingRepository.save(meeting);

        return new Response(true, HttpStatus.CREATED, "Successfully scheduled the meeting");
    }

    /**
     * Retrieves all the agent's scheduled meetings with customers and owners
     * @param username Username of the agent to have meetings retrieved
     * @return List of meetings if present, empty otherwise
     */
    public Optional<List<Meeting>> getAgentScheduledMeetings(String username) {
        return meetingRepository.findByAgentUsername(username);
    }

    /**
     * Retrieves all the agent's scheduled meetings with users who have the specified role
     * @param username Username of the agent
     * @param role Role of the users who are to be retrieved
     * @return List of meetings if present, empty otherwise
     */
    public Optional<List<Meeting>> getAgentScheduledMeetingByRole(String username, Role role) {
        return meetingRepository.findByAgentUsernameAndRole(username, role);
    }

    /**
     * Retrieves all the user's scheduled meetings
     * @param username Username of the user
     * @return List of meetings if present, empty otherwise
     */
    public Optional<List<Meeting>> getMeetings(String username) {
        return meetingRepository.findByUsername(username);
    }
}
