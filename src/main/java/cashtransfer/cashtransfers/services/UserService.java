package cashtransfer.cashtransfers.services;

import cashtransfer.cashtransfers.entities.User;
import org.springframework.boot.actuate.health.Health;

import java.util.Optional;

/**
 * @author Abubakir Dev
 */
public interface UserService {
    boolean authenticate(String email, String password);

    void register(String email, String password);

    Optional<User> findByEmail(String email);

    Health health();
}
