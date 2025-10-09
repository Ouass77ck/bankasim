package main.java.fr.bankasim.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private UUID userId;
    private String nom;
    private String prenom;
    private String email;
    private String rib;
    private BigDecimal solde;
    private BigDecimal decouvertAutorise;
    private BigDecimal plafondTransaction;
}
