package app.estateagency.services;

import app.estateagency.dto.response.Response;
import app.estateagency.jpa.entities.*;
import app.estateagency.jpa.repositories.CustomerMeetingRepository;
import app.estateagency.jpa.repositories.OwnerMeetingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * A service allowing to handle business logic related to meetings scheduling
 */
@Service
@RequiredArgsConstructor
public class MeetingService {
    private final CustomerMeetingRepository customerMeetingRepository;
    private final OwnerMeetingRepository ownerMeetingRepository;
    private final CalendarService calendarService;
    private final CustomerService customerService;
    private final OwnerService ownerService;

    /**
     * Allows customers to schedule meetings with agents
     * @param username Username of the customer who schedules the meeting
     * @param id ID of the available calendar
     * @return Response if successfully scheduled meeting
     */
    @Transactional
    public Response customerScheduleMeeting(String username, Long id) {
        Optional<Calendar> calendar = calendarService.getByID(id);

        if (calendar.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No calendar of the provided ID found");

        Optional<Customer> customer = customerService.getByUsername(username);

        if (customer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No customer of the provided username found");

        CustomerMeeting customerMeeting = new CustomerMeeting();

        customerMeeting.setCustomer(customer.get());
        customerMeeting.setAgent(calendar.get().getAgent());
        customerMeeting.setDate(calendar.get().getDate());

        customerMeetingRepository.save(customerMeeting);
        calendarService.deleteCalendar(calendar.get());

        return new Response(true, HttpStatus.CREATED, "Successfully scheduled meeting");
    }

    /**
     * Allows owners to schedule meetings with agents
     * @param username Username of the owner who schedules the meeting
     * @param id ID of the available calendar
     * @return Response if successfully scheduled meeting
     */
    @Transactional
    public Response ownerScheduleMeeting(String username, Long id) {
        Optional<Calendar> calendar = calendarService.getByID(id);

        if (calendar.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No calendar of the provided ID found");

        Optional<Owner> owner = ownerService.getByUsername(username);

        if (owner.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No owner of the provided username found");

        OwnerMeeting ownerMeeting = new OwnerMeeting();

        ownerMeeting.setOwner(owner.get());
        ownerMeeting.setAgent(calendar.get().getAgent());
        ownerMeeting.setDate(calendar.get().getDate());

        ownerMeetingRepository.save(ownerMeeting);
        calendarService.deleteCalendar(calendar.get());

        return new Response(true, HttpStatus.CREATED, "Successfully scheduled meeting");
    }
}
