package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Απαραίτητο import

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    // Αυτή η μέθοδος λύνει το κόκκινο σφάλμα στο DataSetup
    Optional<Trainer> findByEmail(String email);

    List<Trainer> findByArea(String area);

    List<Trainer> findBySpecialization(String specialization);
}