package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.AgentResponse;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.Agent;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.AgentService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 *  A controller handling requests regarding Agent entity
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService agentService;

    /**
     * Allows admins to add a new agent account
     * @param userDetails Details of the user who sends the request, used to validate required privilege
     * @param userRequest Details of the newly created agent account
     * @return Response if successfully added the new account
     */
    @RequiredPrivilege(Privilege.ADD_AGENT)
    @PostMapping("/admin/add-agent")
    public ResponseEntity<Response> createAgentAccount(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UserRequest userRequest) {
        Response response = agentService.createAgentAccount(userRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows all the users to check available agents
     * @return Response with list of agents if present
     */
    @GetMapping("/auth/agents")
    public ResponseEntity<List<AgentResponse>> getAllAgents() {
        Optional<List<Agent>> agents = agentService.getAllAgents();

        return agents
                .map(agentsList -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(agentsList.stream().map(Mapper.INSTANCE::convertAgent).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    /**
     * Allows to retrieve an agent by their ID
     * @param id ID of the agent to be retrieved
     * @return Agent entity is present
     */
    @GetMapping("/auth/agent")
    public ResponseEntity<AgentResponse> checkAgent(@NotNull @RequestParam("id") Long id) {
        Optional<Agent> optionalAgent = agentService.getByID(id);

        return optionalAgent.map(agent -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Mapper.INSTANCE.convertAgent(agent)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
