package com.encenape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String token) {
        String subject = "EncenaPE - Redefinição de Senha";
        // Esta é a URL do seu frontend Angular
        String resetUrl = "http://localhost:4200/resetar-senha?token=" + token;
        String message = "Você solicitou a redefinição de sua senha.\n\n"
                       + "Clique no link abaixo para redefinir:\n"
                       + resetUrl + "\n\n"
                       + "Se você não solicitou isso, ignore este email.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject(subject);
        email.setText(message);
        
        mailSender.send(email);
    }
}
