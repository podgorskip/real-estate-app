package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.AgentResponse;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.Agent;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @RequiredPrivilege(Privilege.ADD_AGENT)
    @PostMapping("/admin/add-agent")
    public ResponseEntity<Response> createAgentAccount(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UserRequest userRequest) {
        Response response = agentService.createAgentAccount(userRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/auth/agents")
    public ResponseEntity<List<AgentResponse>> getAllAgents(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<Agent>> agents = agentService.getAllAgents();

        return agents
                .map(agentsList -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(agentsList.stream().map(Mapper.INSTANCE::convertAgent).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
