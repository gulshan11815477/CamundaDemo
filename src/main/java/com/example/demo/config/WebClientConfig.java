package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
	
	
	   @Bean
	    public WebClient camundaTasklistClient(@Value("${camunda.tasklist.base-url}") String baseUrl) {
	        return WebClient.builder()
	                .baseUrl(baseUrl)
	                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
	                .filter(logRequest())     
	                .filter(logResponse()) 
	                .build();
	    }
	   
	   
	   private ExchangeFilterFunction logRequest() {
		    return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
		        System.out.println("REQUEST:");
		        System.out.println("URL     : " + clientRequest.url());
		        System.out.println("Method  : " + clientRequest.method());
		        System.out.println("Headers : " + clientRequest.headers());
		        return Mono.just(clientRequest);
		    });
		}

		private ExchangeFilterFunction logResponse() {
		    return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
		        System.out.println("RESPONSE:");
		        System.out.println("Status  : " + clientResponse.statusCode());
		        System.out.println("Headers : " + clientResponse.headers().asHttpHeaders());
		        return Mono.just(clientResponse);
		    });
		}


}
