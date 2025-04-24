package com.yuri.matheus.furiachatbot.service;

import com.yuri.matheus.furiachatbot.controller.GeminiService;
import com.yuri.matheus.furiachatbot.dto.request.ChatRequest;
import com.yuri.matheus.furiachatbot.dto.response.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final GeminiService geminiService;

    public ChatService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public ChatResponse processMessage(ChatRequest request){
        String resposta = geminiService.getGeminiReply(request.message());
        return new ChatResponse(resposta);
    }
}

