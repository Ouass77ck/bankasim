package fr.bankasim.account.client;

import fr.bankasim.account.dto.TransactionDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Component
public class TransactionClient {

    private final RestTemplate restTemplate;

    public TransactionClient() {
        this.restTemplate = new RestTemplate();
    }

    private static final String BASE_URL = "http://localhost:8082/api/transactions";

    public TransactionDTO createTransaction(TransactionDTO dto) {
        ResponseEntity<TransactionDTO> response = restTemplate.postForEntity(BASE_URL, dto, TransactionDTO.class);
        return response.getBody();
    }

    public void validateTransaction(UUID transactionId) {
        restTemplate.put(BASE_URL + "/validate/" + transactionId, null);
    }

    public void refuseTransaction(UUID transactionId) {
        restTemplate.put(BASE_URL + "/refuse/" + transactionId, null);
    }
}
