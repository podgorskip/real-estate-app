package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c " +
            "JOIN User u ON u.id = c.user.id " +
            "WHERE u.username = :username")
    Optional<Customer> findByUsername(@Param("username") String username);
}
