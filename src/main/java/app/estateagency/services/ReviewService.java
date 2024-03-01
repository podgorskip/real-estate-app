package app.estateagency.services;

import app.estateagency.dto.request.ReviewRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.jpa.entities.ArchivedOffer;
import app.estateagency.jpa.entities.Review;
import app.estateagency.jpa.entities.User;
import app.estateagency.jpa.repositories.ReviewRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *  A service allowing to handle business logic related to Review entities
 */
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ArchivedOfferService archivedOfferService;

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

        Optional<ArchivedOffer> archivedOffer = archivedOfferService.getByID(id);

        if (archivedOffer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No archived offer of the provided ID found");

        reviewRepository.save(createReview(user.get(), archivedOffer.get(), reviewRequest));

        return new Response(true, HttpStatus.CREATED, "Successfully reviewed the transaction");
    }

    /**
     * Retrieves reviews of the agent with the specified ID
     * @param id ID of the agent
     * @return List of reviews if present, empty otherwise
     */
    public Optional<List<Review>> checkReviewsByAgentID(Long id) {
        return reviewRepository.findByAgentID(id);
    }

    /**
     * Allows to create a fully populated Review instance
     * @param user User who reviews the transaction
     * @param archivedOffer Archived offer which is reviewed
     * @param reviewRequest Details containing review details
     * @return Fully populated Review instance
     */
    private Review createReview(User user, ArchivedOffer archivedOffer, ReviewRequest reviewRequest)  {
        Review review = new Review();

        review.setUser(user);
        review.setArchivedOffer(archivedOffer);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setRole(user.getRole());

        return review;
    }
}
