package fr.bankasim.account.services;

import fr.bankasim.account.dto.AccountDTO;
import fr.bankasim.account.dto.RegisterRequest;
import fr.bankasim.account.dto.TransferRequest;
import fr.bankasim.account.model.Account;
import fr.bankasim.account.model.Role;
import fr.bankasim.account.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import fr.bankasim.account.client.TransactionClient;
import fr.bankasim.account.dto.TransactionDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TransactionClient transactionClient;


    public Account Register(RegisterRequest request){
        if (accountRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email déjà utilisé !");
        }
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setNom(request.getNom());
        account.setPrenom(request.getPrenom());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setSolde(BigDecimal.ZERO);
        account.setRole(Role.CLIENT);
        account.setPlafondTransaction(BigDecimal.valueOf(1000));
        account.setDecouvertAutorise(BigDecimal.valueOf(1000));
        account.setRib(generateRib(request.getEmail()));

        return accountRepository.save(account);
    }

    private String generateRib(String email) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();

        for (char c : email.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                // a=1, b=2, ..., z=26
                int value = c - 'a' + 1;
                str.append(value);
            } else if (Character.isDigit(c)) {
                str.append(c);
            } else {
                str.append(random.nextInt(10));
            }
        }

        String rib = "FR" + str.toString();
        return rib;
    }

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

        TransactionDTO transaction = TransactionDTO.builder()
                .idEnvoyeur(sender.getUserId())
                .ribDestinataire(receiver.getRib())
                .montant(request.getMontant())
                .description("Virement de " + sender.getNom() + " vers " + receiver.getNom())
                .build();

        TransactionDTO createdTransaction;
        try {
            createdTransaction = transactionClient.createTransaction(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer la transaction dans le service distant", e);
        }

        try {
            if (request.getMontant().compareTo(sender.getPlafondTransaction()) > 0)
                throw new RuntimeException("Montant dépasse le plafond autorisé");

            BigDecimal nouveauSolde = sender.getSolde().subtract(request.getMontant());
            if (nouveauSolde.compareTo(sender.getDecouvertAutorise().negate()) < 0)
                throw new RuntimeException("Découvert dépassé");

            sender.setSolde(nouveauSolde);
            receiver.setSolde(receiver.getSolde().add(request.getMontant()));

            accountRepository.save(sender);
            accountRepository.save(receiver);

            transactionClient.validateTransaction(createdTransaction.getTransactionId());

        } catch (Exception e) {
            transactionClient.refuseTransaction(createdTransaction.getTransactionId());
            throw new RuntimeException("Erreur pendant le transfert : " + e.getMessage(), e);
        }
    }


    public Account setPlafond(BigDecimal newPlaf, UUID id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
        
        account.setPlafondTransaction(newPlaf);
        return accountRepository.save(account);
    }
    public Account setDecouvert(BigDecimal newDec, UUID id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
        
        account.setDecouvertAutorise(newDec);
        return accountRepository.save(account);
    }
    public Account setSolde(BigDecimal moula, UUID id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
        BigDecimal newSolde = moula.add(account.getSolde());
        account.setSolde(newSolde);
        return accountRepository.save(account);
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
