package gr.hua.dit.fittrack.core.security;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ψάχνουμε στη βάση με βάση το emailAddress
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Επιστρέφουμε το αντικείμενο UserDetails που καταλαβαίνει η Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailAddress()) // Το email παίζει το ρόλο του "username"
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}