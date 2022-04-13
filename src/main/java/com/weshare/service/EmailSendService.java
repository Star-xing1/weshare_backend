package com.weshare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("EmailSendService")
public class EmailSendService {
    private JavaMailSender javaMailSender;
    @Autowired
    public EmailSendService(JavaMailSender javaMailSender) {
        this.javaMailSender=javaMailSender;
    }
    @Async
    public void sendEmail (SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}