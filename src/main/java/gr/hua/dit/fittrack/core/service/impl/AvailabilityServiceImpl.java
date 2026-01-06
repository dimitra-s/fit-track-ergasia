package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import gr.hua.dit.fittrack.core.repository.TrainerAvailabilityRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.service.AvailabilityService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAvailabilityResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
    private final TrainerAvailabilityRepository availabilityRepository;
    private final TrainerRepository trainerRepository;

    public AvailabilityServiceImpl(
            final TrainerAvailabilityRepository availabilityRepository,
            final TrainerRepository trainerRepository
    ) {
        if (availabilityRepository == null) throw new NullPointerException();
        if (trainerRepository == null) throw new NullPointerException();

        this.availabilityRepository = availabilityRepository;
        this.trainerRepository = trainerRepository;
    }

    @Transactional
    @Override
    public CreateAvailabilityResult createSlot(Long trainerId, LocalDateTime start, LocalDateTime end) {
        // Έλεγχος εισόδου
        if (start == null || end == null || !start.isBefore(end)) {
            return CreateAvailabilityResult.fail("Invalid start or end time");
        }

        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer == null) {
            return CreateAvailabilityResult.fail("Trainer not found");
        }

        // Έλεγχος overlap
        List<TrainerAvailability> existingSlots = availabilityRepository.findByTrainerId(trainerId);
        boolean overlaps = existingSlots.stream().anyMatch(slot ->
                start.isBefore(slot.getEndTime()) && end.isAfter(slot.getStartTime())
        );

        if (overlaps) {
            return CreateAvailabilityResult.fail("Slot overlaps with existing availability");
        }

        // Δημιουργία slot
        TrainerAvailability slot = new TrainerAvailability();
        slot.setTrainer(trainer);
        slot.setStartTime(start);
        slot.setEndTime(end);

        slot = availabilityRepository.save(slot);

        return CreateAvailabilityResult.success(slot);
    }

    @Override
    public List<TrainerAvailability> listSlotsForTrainer(Long trainerId) {
        return availabilityRepository.findByTrainerId(trainerId);
    }
}