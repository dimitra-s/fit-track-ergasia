package gr.hua.dit.fittrack.core.security;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;

    public ApplicationUserDetailsService(UserRepository userRepository, TrainerRepository trainerRepository) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("LOGIN attempt email=" + email);

        Optional<Trainer> trainer = trainerRepository.findByEmail(email);
        System.out.println("trainer found? " + trainer.isPresent());

        if (trainer.isPresent()) {
            System.out.println("-> LOGGING AS TRAINER");
            Trainer t = trainer.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(t.getEmail())
                    .password(t.getPassword())
                    .roles("TRAINER")
                    .build();
        }

        Optional<User> user = userRepository.findByEmailAddress(email);
        System.out.println("user found? " + user.isPresent());

        if (user.isPresent()) {
            System.out.println("-> LOGGING AS USER role=" + user.get().getRole());
            User u = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(u.getEmailAddress())
                    .password(u.getPassword())
                    .roles(u.getRole().name())
                    .build();
        }

        throw new UsernameNotFoundException("User or Trainer not found with email: " + email);
    }
}

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        // 1. Προσπάθεια εύρεσης σε απλό Χρήστη (User)
//        Optional<User> user = userRepository.findByEmailAddress(email);
//        if (user.isPresent()) {
//            User u = user.get();
//            return org.springframework.security.core.userdetails.User.builder()
//                    .username(u.getEmailAddress())
//                    .password(u.getPassword())
//                    .roles(u.getRole().name()) // Π.χ. ROLE_USER
//                    .build();
//        }
//
//        // 2. Αν δεν βρέθηκε, προσπάθεια εύρεσης σε Trainer
//        Optional<Trainer> trainer = trainerRepository.findByEmail(email);
//        if (trainer.isPresent()) {
//            Trainer t = trainer.get();
//            return org.springframework.security.core.userdetails.User.builder()
//                    .username(t.getEmail())
//                    .password(t.getPassword())
//                    .roles("TRAINER") // Χειροκίνητο role για τους trainers
//                    .build();
//        }
//
//        // 3. Αν δεν βρεθεί πουθενά
//        throw new UsernameNotFoundException("User or Trainer not found with email: " + email);
//    }
//}