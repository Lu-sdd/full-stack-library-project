package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.luv2code.springbootlibrary.entity.Message;

@Service
@Transactional
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail){
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }
}
