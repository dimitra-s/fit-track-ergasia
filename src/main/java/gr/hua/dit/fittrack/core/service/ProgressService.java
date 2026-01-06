package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.ProgressRepository;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;

    public ProgressService(final ProgressRepository progressRepository,
                           final UserRepository userRepository) {
        if (progressRepository == null) throw new NullPointerException();
        if (userRepository == null) throw new NullPointerException();
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
    }

    public ProgressRecord addProgress(Long userId, LocalDate date, Double weight, String notes) {
        if (userId == null) throw new IllegalArgumentException("userId is required");
        if (date == null) throw new IllegalArgumentException("date is required");
        if (weight == null) throw new IllegalArgumentException("weight is required");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        ProgressRecord progressRecord = new ProgressRecord();
        progressRecord.setUser(user);
        progressRecord.setDate(date);
        progressRecord.setWeight(weight);
        progressRecord.setNotes(notes);

        return progressRepository.save(progressRecord);
    }

    public Optional<ProgressRecord> getProgressById(Long id) {
        return progressRepository.findById(id);
    }

    public List<ProgressRecord> getProgressForUser(Long userId) {
        return progressRepository.findByUser_Id(userId);
    }
}
