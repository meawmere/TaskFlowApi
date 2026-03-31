package dev.meawmere.taskflow.repository;

import dev.meawmere.taskflow.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findUserByUsername(String username);

    Boolean existsByUsername(String username);
}
