package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.Role;
import gr.hua.dit.fittrack.core.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);    //οχι σε list γιατί το username είναι μοναδικό

    List<User> findByRole(Role role);

}
