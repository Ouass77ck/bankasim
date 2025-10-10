package fr.bankasim.account.controller;

import fr.bankasim.account.model.Account;
import fr.bankasim.account.dto.AccountDTO;
import fr.bankasim.account.dto.RegisterRequest;
import fr.bankasim.account.dto.TransferRequest;
import fr.bankasim.account.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }
    
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody RegisterRequest request) {
        Account account = accountService.Register(request);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        accountService.transfer(request);
        return ResponseEntity.ok("Transfert effectué avec succès");
    }

    @PutMapping("/plafond/{id}")
    public ResponseEntity<String> setPlafond(@RequestBody BigDecimal newPlafond, @PathVariable UUID id){
        accountService.setPlafond(newPlafond,id);
        return ResponseEntity.ok("Plafond mis à jour");
    }

    @PutMapping("/decouvert/{id}")
    public ResponseEntity<String> setDecouvert(@RequestBody BigDecimal newDecouvert, @PathVariable UUID id){
        accountService.setDecouvert(newDecouvert,id);
        return ResponseEntity.ok("Découvert mis à jour");
    }

    @PutMapping("/changesolde/{id}")
    public ResponseEntity<String> setSolde(@RequestBody BigDecimal newSolde, @PathVariable UUID id){
        accountService.setSolde(newSolde,id);
        return ResponseEntity.ok("Solde augmenté!");
    }
}
