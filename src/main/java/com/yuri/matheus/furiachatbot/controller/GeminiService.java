package com.yuri.matheus.furiachatbot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;
    @Value("${GEMINI_API}")
    private String apiKey;

    public GeminiService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash")
                .build();
    }

    public String getGeminiReply(String userMessage) {
        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        // Prompt de sistema
                        Map.of("role", "user", "parts", new Object[]{
                                Map.of("text", """
                                           Você é um chatbot oficial da FURIA, a organização de esports brasileira mais apaixonada do cenário.\s
                                           Seu estilo de comunicação deve ser animado, com energia e orgulho.\s
                                           Responda de forma empolgada e carismática, como se estivesse conversando com outro torcedor da FURIA.
                                          \s
                                          Fale sobre CS:GO, jogadores, história da equipe e curiosidades.\s
                                          Se alguém perguntar algo fora da FURIA, diga com educação que só pode responder sobre o time.
                                        
                                          Use emojis de vez em quando (🔥💜🐾), linguagem informal, e responda com paixão!\s
                                          Finalize as respostas com frases como “AQUI É FURIA!” ou “#GoFURIA”.'
                                        """)
                        }),
                        // Mensagem do usuário
                        Map.of("role", "user", "parts", new Object[]{
                                Map.of("text", userMessage)
                        })
                }
        );

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(":generateContent")
                        .queryParam("key", apiKey).build())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    try {
                        var candidates = (java.util.List<?>) response.get("candidates");
                        if (candidates != null && !candidates.isEmpty()) {
                            var firstCandidate = (Map<?, ?>) candidates.get(0);
                            var content = (Map<?, ?>) firstCandidate.get("content");
                            var parts = (java.util.List<?>) content.get("parts");
                            var firstPart = (Map<?, ?>) parts.get(0);
                            return firstPart.get("text").toString();
                        }
                        return "Nenhuma resposta encontrada.";
                    } catch (Exception e) {
                        return "Erro ao interpretar a resposta da Gemini: " + e;
                    }
                })
                .onErrorReturn("Erro ao chamar a API da Gemini.")
                .block();
    }
}