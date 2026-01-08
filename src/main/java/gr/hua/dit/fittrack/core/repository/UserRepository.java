package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.Role;
import gr.hua.dit.fittrack.core.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Για register: έλεγχος αν υπάρχει ήδη χρήστης με αυτό το email (case-insensitive)
    boolean existsByEmailAddressIgnoreCase(String emailAddress);

    // Για login: φέρε τον χρήστη με αυτό το email
    Optional<User> findByEmailAddressIgnoreCase(String emailAddress);
    Optional<User> findByEmailAddress(String emailAddress);

    // Αν θες να παίρνεις όλους τους trainers / users
    List<User> findByRole(Role role);
}

