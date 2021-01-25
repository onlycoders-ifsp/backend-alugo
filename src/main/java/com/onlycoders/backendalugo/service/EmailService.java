package com.onlycoders.backendalugo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service("emailService")
public class EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    String fromEmail;
    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String toUser, String subject, String body) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toUser);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);
        helper.setText(body,true);
        javaMailSender.send(message);
    }

    @Async
    public void sendEmailAttachment(String toUser, String subject, String body, String fileName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toUser);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);
        helper.setText(body);
        FileSystemResource file = new FileSystemResource(new File(fileName));
        helper.addAttachment(file.getFilename(), file);//Nome, caminho
        javaMailSender.send(message);
    }
}
