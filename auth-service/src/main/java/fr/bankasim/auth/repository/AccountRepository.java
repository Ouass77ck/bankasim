package fr.bankasim.auth.repository;

import fr.bankasim.auth.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByRib(String rib);
    boolean existsByEmail(String email);
}
