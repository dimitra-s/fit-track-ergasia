package gr.hua.dit.fittrack.core;

import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrainersInitializer {

    @Bean
    CommandLineRunner initTrainers(TrainerRepository trainerRepository) {
        return args -> {
            // Αν η βάση είναι άδεια, πρόσθεσε μερικούς γυμναστές για δοκιμή
            if (trainerRepository.count() == 0) {

                Trainer t1 = new Trainer();
                t1.setFirstName("Γιάννης");
                t1.setLastName("Παπαδόπουλος");
                t1.setSpecialization("CrossFit & Ενδυνάμωση");
                t1.setArea("Καλλιθέα");
                trainerRepository.save(t1);

                Trainer t2 = new Trainer();
                t2.setFirstName("Μαρία");
                t2.setLastName("Δημητρίου");
                t2.setSpecialization("Yoga & Pilates");
                t2.setArea("Αθήνα Κέντρο");
                trainerRepository.save(t2);

                Trainer t3 = new Trainer();
                t3.setFirstName("Νίκος");
                t3.setLastName("Στεργίου");
                t3.setSpecialization("Bodybuilding");
                t3.setArea("Πειραιάς");
                trainerRepository.save(t3);

                System.out.println("✅ Οι αρχικοί γυμναστές προστέθηκαν στη βάση!");
            }
        };
    }
}