package main.java.fr.bankasim.account.services;

import main.java.fr.bankasim.account.dto.AccountDTO;
import main.java.fr.bankasim.account.dto.TransferRequest;
import main.java.fr.bankasim.account.model.Account;
import main.java.fr.bankasim.account.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public AccountDTO getAccountById(UUID id) {
        return accountRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
    }

    @Transactional
    public void transfer(TransferRequest request) {
        Account sender = accountRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Expéditeur introuvable"));
        Account receiver = accountRepository.findByRib(request.getRibDestinataire())
                .orElseThrow(() -> new RuntimeException("Destinataire introuvable"));

        if (request.getMontant().compareTo(sender.getPlafondTransaction()) > 0)
            throw new RuntimeException("Montant dépasse le plafond autorisé");

        BigDecimal nouveauSolde = sender.getSolde().subtract(request.getMontant());
        if (nouveauSolde.compareTo(sender.getDecouvertAutorise().negate()) < 0)
            throw new RuntimeException("Découvert dépassé");

        sender.setSolde(nouveauSolde);
        receiver.setSolde(receiver.getSolde().add(request.getMontant()));

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    private AccountDTO toDTO(Account a) {
        return AccountDTO.builder()
                .userId(a.getUserId())
                .nom(a.getNom())
                .prenom(a.getPrenom())
                .email(a.getEmail())
                .rib(a.getRib())
                .solde(a.getSolde())
                .decouvertAutorise(a.getDecouvertAutorise())
                .plafondTransaction(a.getPlafondTransaction())
                .build();
    }
}
