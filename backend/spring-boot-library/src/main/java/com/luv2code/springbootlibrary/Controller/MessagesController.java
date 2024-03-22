package com.luv2code.springbootlibrary.Controller;

import com.luv2code.springbootlibrary.service.MessageService;
import com.luv2code.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.luv2code.springbootlibrary.entity.Message;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    private MessageService messageService;

    @Autowired
    public MessagesController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value="Authorization") String token,
                            @RequestBody Message messageRequest){
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        messageService.postMessage(messageRequest, userEmail);

    }
}
