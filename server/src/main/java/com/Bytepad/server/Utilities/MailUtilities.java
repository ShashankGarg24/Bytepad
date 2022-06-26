package com.Bytepad.server.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;

@Service
public class MailUtilities {


    @Autowired
    JavaMailSender javaMailSender;


    /**
     * function to send mail to a particular user (admin here)
     * @param userEmail
     * @param mailContent
     * @param subject
     */
    @Async
    public void sendMail(String userEmail, String mailContent, String subject) {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("${spring.mail.username}", "Team Bytepad");
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(mailContent, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
