package app.estateagency.controllers;

import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    /**
     * Allows customers to schedule a meeting with agents
     * @param userDetails User details of the user who makes request, used to validate required privilege
     * @param id ID of the available slot
     * @return Response if successfully scheduled the meeting
     */
    @RequiredPrivilege(Privilege.SCHEDULE_MEETING)
    @PostMapping("/customer/schedule-meeting")
    public ResponseEntity<Response> customerScheduleMeeting(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("id") Long id) {
        Response response = meetingService.customerScheduleMeeting(userDetails.getUsername(), id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows owners to schedule a meeting with agents
     * @param userDetails User details of the user who makes request, used to validate required privilege
     * @param id ID of the available slot
     * @return Response if successfully scheduled the meeting
     */
    @RequiredPrivilege(Privilege.SCHEDULE_MEETING)
    @PostMapping("/owner/schedule-meeting")
    public ResponseEntity<Response> ownerScheduleMeeting(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("id") Long id) {
        Response response = meetingService.ownerScheduleMeeting(userDetails.getUsername(), id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
