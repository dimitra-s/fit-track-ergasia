package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.ProgressRepository;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.ProgressService;
import gr.hua.dit.fittrack.core.service.impl.dto.AddProgressRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;

    public ProgressServiceImpl (
            final ProgressRepository progressRepository,
            final UserRepository userRepository
    ) {
        if (progressRepository == null) throw new NullPointerException();
        if (userRepository == null) throw new NullPointerException();

        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
    }

    // ------------------------
    // 1. addProgress(userId, dto)
    // ------------------------
    @Override
    @Transactional
    public ProgressRecord addProgress(Long userId, AddProgressRequest dto) {

        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        if (dto == null) {
            throw new IllegalArgumentException("progress data is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found: " + userId)
                );

        ProgressRecord record = new ProgressRecord();
        record.setUser(user);
        record.setDate(dto.date());
        record.setWeight(dto.weight());
        record.setNotes(dto.notes());

        return progressRepository.save(record);
    }

    // ------------------------
    // 2. getProgressForUser(userId)
    // ------------------------
    @Override
    public List<ProgressRecord> getProgressForUser(Long userId) {

        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }

        return progressRepository.findByUser_Id(userId);
    }
}
