package app.estateagency.services;

import app.estateagency.dto.request.ReviewRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.jpa.entities.HistoryOffer;
import app.estateagency.jpa.entities.Review;
import app.estateagency.jpa.entities.User;
import app.estateagency.jpa.repositories.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *  A service allowing to handle business logic related to Review entities
 */
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final HistoryOfferService historyOfferService;

    /**
     * Allows to review an offer from the finished transactions
     * @param username Username of the user who reviews offers
     * @param id ID of the offer to be reviewed
     * @param reviewRequest Details of the review
     * @return Response if successfully posted the offer
     */
    @Transactional
    public Response reviewTransaction(String username, Long id, ReviewRequest reviewRequest) {
        Optional<User> user = userService.getByUsername(username);

        if (user.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No account of the provided username found");


    }

    private Review createReview(User user, HistoryOffer historyOffer, ReviewRequest reviewRequest)
}
