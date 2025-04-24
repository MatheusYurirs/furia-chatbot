package com.yuri.matheus.furiachatbot.controller;

import com.yuri.matheus.furiachatbot.dto.request.ChatRequest;
import com.yuri.matheus.furiachatbot.dto.response.ChatResponse;
import com.yuri.matheus.furiachatbot.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> handleMessage(@RequestBody ChatRequest request){
        ChatResponse response = chatService.processMessage(request);
        return  ResponseEntity.ok(response);
    }
}
