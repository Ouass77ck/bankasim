package fr.bankasim.account.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

import fr.bankasim.account.model.Role;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String rib;

    @Column(nullable = false)
    private BigDecimal solde;

    @Column(name = "decouvert_autorise")
    private BigDecimal decouvertAutorise;

    @Column(name = "plafond_transaction")
    private BigDecimal plafondTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
}
