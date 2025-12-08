package com.example.demo.config;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import java.util.Map;

public class CamundaTokenFetcher {

    private static final String TOKEN_URL = "https://login.cloud.camunda.io/oauth/token";

    private static final String CLIENT_ID = "LZIpZW_1SiQE4QAgxsa1cjn-495z9fOH";
    private static final String CLIENT_SECRET = "OlzwvZPrVu49A.~AONH2ipjufgBn.~u.tbIWOmIjlvh.dCEVhMXWLLSSe.TOlM7U";
    private static final String AUDIENCE = "tasklist.camunda.io";
    private static final String GRANT_TYPE = "client_credentials";

    public static Mono<String> fetchAccessToken() {
        WebClient webClient = WebClient.builder().build();

        return webClient.post()
                .uri(TOKEN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "client_id", CLIENT_ID,
                        "client_secret", CLIENT_SECRET,
                        "audience", AUDIENCE,
                        "grant_type", GRANT_TYPE
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"));
    }
    
	public static String fetchAccessTokenString() {
		System.out.println("generating token");
		Mono<String> monoStringMono = fetchAccessToken();
		return monoStringMono.block();

	}
}