package fr.bankasim.transaction.dto;

import fr.bankasim.transaction.model.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private UUID transactionId;
    private UUID idEnvoyeur;
    private String ribDestinataire;
    private BigDecimal montant;
    private LocalDateTime date;
    private TransactionStatus status;
    private String description;
}
