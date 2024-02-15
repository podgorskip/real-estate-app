package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.response.MeetingResponse;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.enums.Role;
import app.estateagency.jpa.entities.Meeting;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.MeetingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 *  A controller handling requests regarding Meeting entities
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    /**
     * Allows owners and customers to schedule a meeting with agents
     * @param userDetails User details of the user who makes request, used to validate required privilege
     * @param id ID of the available slot
     * @return Response if successfully scheduled the meeting
     */
    @RequiredPrivilege(Privilege.SCHEDULE_MEETING)
    @PostMapping("/schedule-meeting")
    public ResponseEntity<Response> scheduleMeeting(@AuthenticationPrincipal UserDetails userDetails, @NonNull @RequestParam("id") Long id) {
        Response response = meetingService.scheduleMeeting(userDetails.getUsername(), id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Retrieves all the user's scheduled meetings
     * @param userDetails User details of the user to have meetings retrieved
     * @return List of meetings if present, empty otherwise
     */
    @RequiredPrivilege(Privilege.CHECK_MEETINGS)
    @GetMapping("/scheduled-meetings")
    public ResponseEntity<List<MeetingResponse>> checkScheduledMeetings(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<Meeting>> optionalMeetings = meetingService.getMeetings(userDetails.getUsername());

        return optionalMeetings.
                map(meetings -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(meetings.stream().map(Mapper.INSTANCE::convertMeetings).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    /**
     * Retrieves all the agent's scheduled meetings
     * @param userDetails User details of the agent to have meetings retrieved
     * @return List of meetings if present, empty otherwise
     */
    @RequiredPrivilege(Privilege.CHECK_MEETINGS)
    @GetMapping("/agent/scheduled-meetings")
    public ResponseEntity<List<MeetingResponse>> checkAgentScheduledMeetings(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<Meeting>> optionalMeetings = meetingService.getAgentScheduledMeetings(userDetails.getUsername());

        return optionalMeetings.
                map(meetings -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(meetings.stream().map(Mapper.INSTANCE::convertMeetings).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    /**
     * Retrieves all the agent's scheduled meetings with users of the specified role
     * @param userDetails User details of the user to have meetings retrieved
     * @param role Role of the user
     * @return List of meetings if present, empty otherwise
     */
    @RequiredPrivilege(Privilege.CHECK_MEETINGS)
    @GetMapping("/agent/scheduled-meetings/{role}")
    public ResponseEntity<List<MeetingResponse>> checkAgentScheduledMeetingsByRole(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("role") String role) {
        Optional<List<Meeting>> optionalMeetings = meetingService.getAgentScheduledMeetingByRole(userDetails.getUsername(), Role.valueOf(role.toUpperCase()));

        return optionalMeetings.
                map(meetings -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(meetings.stream().map(Mapper.INSTANCE::convertMeetings).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
