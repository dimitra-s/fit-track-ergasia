package gr.hua.dit.fittrack.core;

import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.env.Environment;   // ⬅️ ΝΕΟ import

@Configuration
public class TrainersInitializer {

    @Bean
    CommandLineRunner initTrainers(
            TrainerRepository trainerRepository,
            PasswordEncoder passwordEncoder,
            Environment env                       // ⬅️ ΝΕΑ παράμετρος
    ) {
        return args -> {

            // ⬇️ ΑΥΤΟ ΘΕΛΟΥΜΕ ΝΑ ΔΟΥΜΕ
            System.out.println("=== DB URL ===");
            System.out.println(env.getProperty("spring.datasource.url"));
            System.out.println("==============");

            if (trainerRepository.count() == 0) {

                Trainer t1 = new Trainer();
                t1.setFirstName("Γιάννης");
                t1.setLastName("Παπαδόπουλος");
                t1.setEmail("giannis@fittrack.gr");
                t1.setPassword(passwordEncoder.encode("123456"));
                t1.setSpecialization("CrossFit & Ενδυνάμωση");
                t1.setArea("Καλλιθέα");
                t1.setRole("ROLE_TRAINER");
                trainerRepository.save(t1);

                Trainer t2 = new Trainer();
                t2.setFirstName("Μαρία");
                t2.setLastName("Δημητρίου");
                t2.setEmail("maria@fittrack.gr");
                t2.setPassword(passwordEncoder.encode("123456"));
                t2.setSpecialization("Yoga & Pilates");
                t2.setArea("Αθήνα Κέντρο");
                t2.setRole("ROLE_TRAINER");
                trainerRepository.save(t2);

                System.out.println("✅ Οι Trainers προστέθηκαν στη βάση!");
            }
        };
    }
}
