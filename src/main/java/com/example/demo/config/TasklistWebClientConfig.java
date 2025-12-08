package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TasklistWebClientConfig {

	@Value("${tasklist.api.url}")
	private String tasklistApiUrl;

	@Bean
	public WebClient tasklistWebClient() {
		return WebClient.builder().baseUrl(tasklistApiUrl) 
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}
