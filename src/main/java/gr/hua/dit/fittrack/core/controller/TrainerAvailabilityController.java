package gr.hua.dit.fittrack.core.controller;

import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import gr.hua.dit.fittrack.core.security.CurrentUserProvider;
import gr.hua.dit.fittrack.core.service.AvailabilityService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAvailabilityResult;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trainer/availability")
public class TrainerAvailabilityController {

    private final AvailabilityService availabilityService;
    private final CurrentUserProvider currentUserProvider;

    public TrainerAvailabilityController(
            final AvailabilityService availabilityService,
            final CurrentUserProvider currentUserProvider
    ) {
        if (availabilityService==null) throw new NullPointerException();
        if (currentUserProvider==null) throw new NullPointerException();

        this.availabilityService = availabilityService;
        this.currentUserProvider = currentUserProvider;
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("")
    public String list(final Model model) {
        final Long trainerId = this.currentUserProvider.requiredTrainerId();
        final List<TrainerAvailability> slots =
                this.availabilityService.listSlotsForTrainer(trainerId);

        model.addAttribute("slots", slots);
        return "availability";
    }

    @GetMapping("/new")
    public String showCreateForm(final Model model) {
        model.addAttribute("form", new AvailabilityForm(null, null));
        return "availability_new";
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/new")
    public String handleCreateForm(
            @ModelAttribute("form") @Valid final AvailabilityForm form,
            final BindingResult bindingResult,
            final Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "availability_new";
        }

        final Long trainerId = this.currentUserProvider.requiredTrainerId();

        final CreateAvailabilityResult result =
                this.availabilityService.createSlot(
                        trainerId,
                        form.getStartTime(),
                        form.getEndTime()
                );

        if (!result.created()) {
            model.addAttribute("errorMessage", result.reason());
            return "availability_new";
        }

        return "redirect:/trainer/availability";
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/{slotId}/delete")
    public String delete(@PathVariable final Long slotId) {
        final Long trainerId = this.currentUserProvider.requiredTrainerId();
        this.availabilityService.deleteSlot(trainerId, slotId);
        return "redirect:/trainer/availability";
    }
}