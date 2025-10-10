package fr.bankasim.transaction.controller;

import fr.bankasim.transaction.dto.TransactionDTO;
import fr.bankasim.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(transactionService.createTransaction(dto));
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable UUID transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

    @GetMapping("/rib/{ribDest}")
    public ResponseEntity<List<TransactionDTO>> getTransactionByRibDestinaire(@PathVariable String ribDest) {
        return ResponseEntity.ok(transactionService.getTransactionByRibDestinaire(ribDest));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }

    @PutMapping("/validate/{transactionId}")
    public ResponseEntity<String> validateTransaction(@PathVariable UUID transactionId) {
        transactionService.validateTransaction(transactionId);
        return ResponseEntity.ok("Transaction validée");
    }

    @PutMapping("/refuse/{transactionId}")
    public ResponseEntity<String> refuseTransaction(@PathVariable UUID transactionId) {
        transactionService.refuseTransaction(transactionId);
        return ResponseEntity.ok("Transaction refusée, sale pauvre");
    }

}
