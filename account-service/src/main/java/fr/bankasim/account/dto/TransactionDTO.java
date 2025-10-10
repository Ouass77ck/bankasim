package fr.bankasim.account.dto;

import lombok.*;

import java.math.BigDecimal;
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
    private String description;
}
