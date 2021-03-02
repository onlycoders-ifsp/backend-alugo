package com.onlycoders.backendalugo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

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
    public void sendEmail(String toUser, String subject, String body) throws MessagingException, IOException {
        String path = System.getProperty("user.dir") +
                File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" +
                File.separator + "onlycoders" + File.separator + "backendalugo" + File.separator +
                "model" + File.separator + "entity" + File.separator + "email" + File.separator +
                "templatesEmails" + File.separator + "images" + File.separator;

        MimeMultipart content = new MimeMultipart("related");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeBodyPart bodyMail = new MimeBodyPart();
        bodyMail.setContent(body,"text/html");

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toUser);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);
        helper.setText(body,true);

        MimeBodyPart imgAlugo6 = new MimeBodyPart();
        imgAlugo6.attachFile(path + "alugo6.png");
        imgAlugo6.setContentID("<alugo6>");
        imgAlugo6.setDisposition(MimeBodyPart.INLINE);
        MimeBodyPart imgAlugo3 = new MimeBodyPart();
        imgAlugo3.attachFile(path + "alugo3.png");
        imgAlugo3.setContentID("<alugo3>");
        imgAlugo3.setDisposition(MimeBodyPart.INLINE);
        content.addBodyPart(bodyMail);
        content.addBodyPart(imgAlugo3);
        content.addBodyPart(imgAlugo6);
        message.setContent(content);
        javaMailSender.send(message);
    }

    @Async
    public void sendEmailAttachment(String toUser, String subject, String body, String fileName) throws MessagingException, IOException {
        String path = System.getProperty("user.dir") +
                File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" +
                File.separator + "onlycoders" + File.separator + "backendalugo" + File.separator +
                "model" + File.separator + "entity" + File.separator + "email" + File.separator +
                "templatesEmails" + File.separator + "images" + File.separator;
        //String imagesPath = path + "\\" + "src/main/java/com/onlycoders/backendalugo/model/entity/email/templatesEmails/images/";
        System.out.println(path);

        MimeMultipart content = new MimeMultipart("related");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeBodyPart bodyMail = new MimeBodyPart();
        bodyMail.setContent(body,"text/html");

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toUser);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);
        helper.setText(body,true);

        MimeBodyPart imgAlugo6 = new MimeBodyPart();
        imgAlugo6.attachFile(path + "alugo6.png");
        imgAlugo6.setContentID("<alugo6>");
        imgAlugo6.setDisposition(MimeBodyPart.INLINE);
        MimeBodyPart imgAlugo3 = new MimeBodyPart();
        imgAlugo3.attachFile(path + "alugo3.png");
        imgAlugo3.setContentID("<alugo3>");
        imgAlugo3.setDisposition(MimeBodyPart.INLINE);
        content.addBodyPart(bodyMail);
        content.addBodyPart(imgAlugo3);
        content.addBodyPart(imgAlugo6);
        message.setContent(content);

        FileSystemResource file = new FileSystemResource(new File(fileName));
        helper.addAttachment(file.getFilename(), file);//Nome, caminho
        javaMailSender.send(message);
    }
}
