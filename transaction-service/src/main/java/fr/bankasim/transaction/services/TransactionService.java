package fr.bankasim.transaction.service;

import fr.bankasim.transaction.dto.TransactionDTO;
import fr.bankasim.transaction.model.Transaction;
import fr.bankasim.transaction.model.TransactionStatus;
import fr.bankasim.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionDTO createTransaction(TransactionDTO dto) {
        Transaction transaction = Transaction.builder()
                .idEnvoyeur(dto.getIdEnvoyeur())
                .ribDestinataire(dto.getRibDestinataire())
                .montant(dto.getMontant())
                .date(LocalDateTime.now())
                .status(TransactionStatus.EN_ATTENTE)
                .description(dto.getDescription())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return toDTO(saved);
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public TransactionDTO getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Transaction introuvable"));
    }

    public List<TransactionDTO> getTransactionsByUser(UUID userId) {
        return transactionRepository.findAllByIdEnvoyeur(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TransactionDTO> getTransactionByRibDestinaire(String rib) {
    return transactionRepository.findAllByRibDestinataire(rib)
            .stream()
            .map(this::toDTO)
            .toList();
}

    private TransactionDTO toDTO(Transaction t) {
        return TransactionDTO.builder()
                .transactionId(t.getTransactionId())
                .idEnvoyeur(t.getIdEnvoyeur())
                .ribDestinataire(t.getRibDestinataire())
                .montant(t.getMontant())
                .date(t.getDate())
                .status(t.getStatus())
                .description(t.getDescription())
                .build();
    }

    @Transactional
    public void validateTransaction(UUID transactionId) {
        Transaction t = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction introuvable"));
        
        t.setStatus(TransactionStatus.VALIDEE);
        t.setDescription(t.getDescription() + " ✅");

        transactionRepository.save(t);
    }

    @Transactional
    public void refuseTransaction(UUID transactionId) {
        Transaction t = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction introuvable"));
        
        t.setStatus(TransactionStatus.REFUSEE);
        t.setDescription(t.getDescription() + " ❌");

        transactionRepository.save(t);
    }
    
}
