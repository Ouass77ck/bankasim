package fr.bankasim.account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String email;
    private String password;
}
