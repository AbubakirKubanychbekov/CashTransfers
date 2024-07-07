package cashtransfer.cashtransfers.services.impl;

import cashtransfer.cashtransfers.entities.User;
import cashtransfer.cashtransfers.enums.Role;
import cashtransfer.cashtransfers.repositories.UserRepository;
import cashtransfer.cashtransfers.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Abubakir Dev
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    /**
     * Здесь проверяем приложение с Actuator ом
     */
    @Override
    public Health health() {
        boolean healthCheckResult = true;

        if (healthCheckResult) {
            return Health.up().build();
        } else {
            return Health.down().withDetail("Error", "Something went wrong").build();
        }
    }

    public boolean authenticate(String email, String password) {
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return true;
        }
        return false;
    }

    public void register(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists");
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.OWNER_CARD)
                .build();
        userRepository.save(user);
        logger.info("Register is successfully saved");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        logger.error("User by email " +  email + " not found");
        return Optional.ofNullable(userRepository.getUserByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found")));
    }
}
