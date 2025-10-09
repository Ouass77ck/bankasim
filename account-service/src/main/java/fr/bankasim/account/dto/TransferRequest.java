package main.java.fr.bankasim.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    private UUID senderId;
    private String ribDestinataire;
    private BigDecimal montant;
}