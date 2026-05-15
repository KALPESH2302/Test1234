package in.org.rebit.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KavachRestImpl {

    public String fetchKavachDetailsAPI(String url, String token) {
        String response = "";
        try {
            response = WebClient.builder().build().get()
                    .uri(url)
                    .header("Authorization", "Bearer " + token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return response;
    }
}
