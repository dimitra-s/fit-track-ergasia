package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAvailabilityResult;

import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityService {

    CreateAvailabilityResult createSlot(Long trainerId, LocalDateTime start, LocalDateTime end);

    List<TrainerAvailability> listSlotsForTrainer(Long trainerId);
}
