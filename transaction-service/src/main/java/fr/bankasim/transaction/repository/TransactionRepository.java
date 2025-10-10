package fr.bankasim.transaction.repository;

import fr.bankasim.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findAllByIdEnvoyeur(UUID idEnvoyeur);
    List<Transaction> findAllByRibDestinataire(String ribDestinataire);
}
