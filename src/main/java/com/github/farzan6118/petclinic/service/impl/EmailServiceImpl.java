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

    /**
     * OWNER — Send when a pet becomes due for a periodic checkup.
     */
    @Override
    public void sendCheckupReminder(
            String email,
            String petName,
            LocalDate checkupDate
    ) {
        Context context = new Context();
        context.setVariable("petName", petName);
        context.setVariable("checkupDate", checkupDate);

        sendHtmlEmail(
                email,
                "Periodic Checkup Reminder",
                "email/checkup-reminder",
                context
        );

        log.info("Checkup reminder sent: {}", petName);
    }

    /**
     * OWNER — Send immediately after a veterinary visit is scheduled.
     */
    @Override
    public void sendVisitScheduledNotification(
            String email,
            String ownerName,
            String petName,
            LocalDateTime visitDate,
            String vetName
    ) {
        Context context = new Context();
        context.setVariable("ownerName", petName);
        context.setVariable("petName", petName);
        context.setVariable("vetName", vetName);
        context.setVariable("appointmentDate", visitDate);

        sendHtmlEmail(
                email,
                "Veterinary Visit Scheduled",
                "email/pet-clinic-appointment-scheduled",
                context
        );

        log.info("Visit scheduled notification sent: {}", petName);
    }

    /**
     * VET — Send immediately after a new appointment is assigned to the vet.
     */
    @Override
    public void sendVetAppointmentScheduledNotification(
            String vetEmail,
            String vetName,
            LocalDateTime appointmentDate,
            String petName,
            String petType,
            String ownerName
    ) {
        Context context = new Context();
        context.setVariable("vetName", vetName);
        context.setVariable("petName", petName);
        context.setVariable("petType", petType);
        context.setVariable("ownerName", ownerName);
        context.setVariable("appointmentDate", appointmentDate);

        sendHtmlEmail(
                vetEmail,
                "New Veterinary Appointment",
                "email/visit-vet-scheduled",
                context
        );

        log.info("Vet appointment scheduled: {}", petName);
    }

    /**
     * OWNER — Send immediately after an existing visit is rescheduled.
     */
    @Override
    public void sendVisitRescheduledNotification(
            String email,
            String petName,
            String vetName,
            LocalDateTime previousVisitDate,
            LocalDateTime newVisitDate
    ) {
        Context context = new Context();
        context.setVariable("petName", petName);
        context.setVariable("vetName", vetName);
        context.setVariable("previousVisitDate", previousVisitDate);
        context.setVariable("newVisitDate", newVisitDate);

        sendHtmlEmail(
                email,
                "Veterinary Visit Rescheduled",
                "email/visit-rescheduled",
                context
        );

        log.info("Visit rescheduled notification sent: {}", petName);
    }

    /**
     * VET — Send immediately after an appointment assigned to the vet is rescheduled.
     */
    @Override
    public void sendVetAppointmentRescheduledNotification(
            String vetEmail,
            String vetName,
            String petName,
            String petType,
            String ownerName,
            LocalDateTime previousAppointmentDate,
            LocalDateTime newAppointmentDate
    ) {
        Context context = new Context();
        context.setVariable("vetName", vetName);
        context.setVariable("ownerName", ownerName);
        context.setVariable("petName", petName);
        context.setVariable("previousAppointmentDate", previousAppointmentDate);
        context.setVariable("newAppointmentDate", newAppointmentDate);

        sendHtmlEmail(
                vetEmail,
                "Veterinary Appointment Rescheduled",
                "email/vet-appointment-rescheduled",
                context
        );

        log.info("Vet appointment rescheduled: {}", petName);
    }

    /**
     * OWNER — Send immediately after the owner's visit is canceled.
     */
    @Override
    public void sendVisitCancelledNotification(
            String email,
            String petName,
            String vetName,
            LocalDateTime visitDate,
            String cancellationReason
    ) {
        Context context = new Context();
        context.setVariable("petName", petName);
        context.setVariable("vetName", vetName);
        context.setVariable("visitDate", visitDate);
        context.setVariable("cancellationReason", cancellationReason);

        sendHtmlEmail(
                email,
                "Veterinary Visit Cancelled",
                "email/visit-cancelled",
                context
        );

        log.info("Visit cancellation notification sent: {}", petName);
    }

    /**
     * VET — Send immediately after an appointment assigned to the vet is canceled.
     */
    @Override
    public void sendVetAppointmentCancelledNotification(
            String vetEmail,
            String vetName,
            String petName,
            String ownerName,
            LocalDateTime appointmentDate,
            String cancellationReason
    ) {
        Context context = new Context();
        context.setVariable("vetName", vetName);
        context.setVariable("petName", petName);
        context.setVariable("ownerName", ownerName);
        context.setVariable("appointmentDate", appointmentDate);
        context.setVariable("cancellationReason", cancellationReason);

        sendHtmlEmail(
                vetEmail,
                "Veterinary Appointment Cancelled",
                "email/vet-appointment-cancelled",
                context
        );

        log.info("Vet appointment cancellation sent: {}", petName);
    }

    /**
     * INTERNAL — Common method used by all email operations.
     * Do not call this directly from the business/service layer.
     */
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
            log.error("Email failed: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}




