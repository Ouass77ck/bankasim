package fr.bankasim.auth.service;

import fr.bankasim.auth.dto.AuthRequest;
import fr.bankasim.auth.dto.AuthResponse;
import fr.bankasim.auth.model.Account;
import fr.bankasim.auth.repository.AccountRepository;
import fr.bankasim.auth.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse login(AuthRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtService.generateToken(account);

        return AuthResponse.builder()
                .token(token)
                .userId(account.getUserId().toString())
                .role(account.getRole().toString())
                .build();
    }
}
