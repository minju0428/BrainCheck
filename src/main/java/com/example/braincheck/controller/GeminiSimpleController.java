package com.example.braincheck.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.List;

@RestController
public class GeminiSimpleController {
    private final WebClient webClient = WebClient.create("https://generativelanguage.googleapis.com");

    @Value("${gemini.api.key}")
    private String apiKey;

    //ai에게 질문하는 부분
    @PostMapping("/gemini/simple")
    public Mono<String> callGemini(@RequestBody String input) {
        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        //질문
                        "parts", List.of(Map.of("text", "간단히 대답해줘:\n\n" + input))
                ))
        );

        //ai의 대답을 받아오는 부분
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);
    }
}
