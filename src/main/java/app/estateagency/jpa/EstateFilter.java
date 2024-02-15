package app.estateagency.jpa;

import app.estateagency.enums.estate.Availability;
import app.estateagency.enums.estate.Condition;
import app.estateagency.enums.estate.EstateType;
import app.estateagency.jpa.entities.Estate;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class used to filter estates based on the passed criteria
 */
public class EstateFilter {
    /**
     * Returns specification with applied predicates passed as arguments
     * @return Specification with predicates used to filter estates
     */
    public static Specification<Estate> filterEstates(
            String type,
            Integer bathrooms,
            Integer rooms,
            Boolean garage,
            Integer storey,
            String location,
            Boolean balcony,
            String availability,
            Double size,
            String condition,
            Double priceFrom,
            Double priceTo,
            LocalDateTime postFrom,
            LocalDateTime postTo
    ) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(type)) predicates.add(criteriaBuilder.equal(root.get("type"), EstateType.valueOf(type.toUpperCase())));
            if (Objects.nonNull(bathrooms)) predicates.add(criteriaBuilder.equal(root.get("bathrooms"), bathrooms));
            if (Objects.nonNull(rooms)) predicates.add(criteriaBuilder.equal(root.get("rooms"), rooms));
            if (Objects.nonNull(garage)) predicates.add(criteriaBuilder.equal(root.get("garage"), garage));
            if (Objects.nonNull(storey)) predicates.add(criteriaBuilder.equal(root.get("storey"), storey));
            if (Objects.nonNull(location)) predicates.add(criteriaBuilder.equal(root.get("location"), location));
            if (Objects.nonNull(balcony)) predicates.add(criteriaBuilder.equal(root.get("balcony"), balcony));
            if (Objects.nonNull(availability)) predicates.add(criteriaBuilder.equal(root.get("availability"), Availability.valueOf(availability.toUpperCase())));
            if (Objects.nonNull(size)) predicates.add(criteriaBuilder.equal(root.get("size"), size));
            if (Objects.nonNull(condition)) predicates.add(criteriaBuilder.equal(root.get("condition"), Condition.valueOf(condition.toUpperCase())));
            if (Objects.nonNull(priceFrom)) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceFrom));
            if (Objects.nonNull(priceTo)) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo));
            if (Objects.nonNull(postFrom)) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("postDate"), postFrom));
            if (Objects.nonNull(postTo)) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("postDate"), postTo));

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        });
    }
}
