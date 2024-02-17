package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.request.ReviewRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.dto.response.ReviewResponse;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.Review;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 *  A controller handling requests regarding Review entities
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * Allows customers and owners to review agents b
     * @param userDetails User details of the user making the request
     * @param id ID of the archived offer which is to be reviewed
     * @return Response if successfully reviewed the transaction
     */
    @RequiredPrivilege(Privilege.REVIEW_AGENT)
    @PostMapping("/review")
    public ResponseEntity<Response> reviewAgent(@AuthenticationPrincipal UserDetails userDetails, @NotNull @RequestParam("id") Long id, @Valid @RequestBody ReviewRequest reviewRequest) {
        Response response = reviewService.reviewTransaction(userDetails.getUsername(), id, reviewRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Retrieves reviews of agents
     * @param id ID of the agent
     * @return List of reviews if present
     */
    @GetMapping("/auth/reviews")
    public ResponseEntity<List<ReviewResponse>> checkReviews(@NotNull @RequestParam("id") Long id) {
        Optional<List<Review>> optionalReviews = reviewService.checkReviewsByAgentID(id);

        return optionalReviews
                .map(reviews -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(reviews.stream().map(Mapper.INSTANCE::convertReview).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
