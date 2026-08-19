package com.github.farzan6118.petclinic.service.impl;

import com.github.farzan6118.petclinic.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void sendCheckupReminder(String email, String petName, LocalDate checkupDate) {
        Context context = new Context();
        context.setVariable("petName", petName);
        context.setVariable("checkupDate", checkupDate);

        sendHtmlEmail(
                email,
                "Periodic checkup reminder",
                "email/checkup-reminder",
                context
        );

        log.info("Checkup reminder sent to [{}]", email);
    }

    @Override
    public void sendVisitReminder(
            String email,
            String petName,
            LocalDateTime visitDate,
            String vetName
    ) {
        Context context = new Context();
        context.setVariable("petName", petName);
        context.setVariable("visitDate", visitDate);
        context.setVariable("vetName", vetName);

        sendHtmlEmail(
                email,
                "Vet visit reminder",
                "email/visit-reminder",
                context
        );

        log.info("Visit reminder sent to [{}]", email);
    }

    @Override
    public void sendVetAppointmentReminder(
            String email,
            String vetName,
            LocalDateTime appointmentDate
    ) {
        Context context = new Context();
        context.setVariable("vetName", vetName);
        context.setVariable("appointmentDate", appointmentDate);

        sendHtmlEmail(
                email,
                "Upcoming appointment",
                "email/vet-appointment-reminder",
                context
        );

        log.info("Vet appointment reminder sent to [{}]", email);
    }

    private void sendHtmlEmail(
            String to,
            String subject,
            String templateName,
            Context context
    ) {
        try {
            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            log.error("Failed to send email to [{}]", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

