package com.example.demo.config;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.model.OAuthTokenResponse;

@Service
public class CamundaAuthService {

	private final WebClient webClient = WebClient.create();

	@Value("${camunda.auth.url}")
	private String authUrl;

	@Value("${camunda.client.auth.client-id}")
	private String clientId;

	@Value("${camunda.client.auth.client-secret}")
	private String clientSecret;

	@Value("${camunda.client.auth.audience}")
	private String audience;

	// Cache key = audience, value = token + expiry
	private final Map<String, CachedToken> cache = new ConcurrentHashMap<>();

	public String getToken() {
		return getTokenForAudience(audience);
	}

	public String getTokenForAudience(String aud) {

		CachedToken cached = cache.get(aud);

		// 1️⃣ If token exists AND not expired → return it
		if (cached != null && !cached.isExpired()) {
			return cached.token();
		}

		// 2️⃣ Else create new token
		return generateNewToken(aud);
	}

	private synchronized String generateNewToken(String aud) {
		// Double-check after locking
		CachedToken existing = cache.get(aud);
		if (existing != null && !existing.isExpired()) {
			return existing.token();
		}

		OAuthTokenResponse response = webClient
				.post().uri(authUrl).bodyValue(Map.of("grant_type", "client_credentials", "audience", aud, "client_id",
						clientId, "client_secret", clientSecret))
				.retrieve().bodyToMono(OAuthTokenResponse.class).block();

		String token = response.getAccessToken();

		// Token is valid for 15 min → set expiry 1 min earlier for safety
		Instant expiry = Instant.now().plus(Duration.ofMinutes(14));

		cache.put(aud, new CachedToken(token, expiry));

		return token;
	}

	public void invalidateToken(String aud) {
		cache.remove(aud);
	}

	private record CachedToken(String token, Instant expiry) {
		boolean isExpired() {
			return Instant.now().isAfter(expiry);
		}
	}
}
