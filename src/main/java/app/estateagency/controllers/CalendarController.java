package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.request.CalendarRequest;
import app.estateagency.dto.response.CalendarResponse;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.Calendar;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.CalendarService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 *  A controller handling requests regarding Calendar entities
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    /**
     * Allows agents to add available meeting slots
     * @param userDetails User details of the user who makes request, used to validate required privilege
     * @param calendarRequest Details containing available slots
     * @return Response is successfully added available slots
     */
    @RequiredPrivilege(Privilege.ADD_MEETINGS)
    @PostMapping("/agent/add-meeting-slots")
    public ResponseEntity<Response> addMeetingSlots(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CalendarRequest calendarRequest) {
        Response response = calendarService.addAvailableMeetings(userDetails.getUsername(), calendarRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows users to check agent's calendar
     * @param userDetails User details of the user who makes request, used to validate required privilege
     * @param id ID of the agent to have the calendar checked
     * @return List of available meetings is present
     */
    @RequiredPrivilege(Privilege.CHECK_CALENDAR)
    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarResponse>> checkAgentCalendar(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("id") Long id) {
        Optional<List<Calendar>> optionalCalendars = calendarService.checkAgentCalendar(id);

        return optionalCalendars
                .map(calendars -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(calendars.stream().map(Mapper.INSTANCE::convertCalendar).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
