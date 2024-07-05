package cn.hein.core.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Email service for sending various types of emails using Spring and Freemarker.
 *
 * @author Hein
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * Sends a simple plain text email.
     *
     * @param to      Recipient's email address
     * @param subject Email subject
     * @param text    Email body text
     */
    public void sendSimpleMail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(to);
            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send simple email", e);
        }
    }

    /**
     * Sends an HTML email using a Freemarker template.
     *
     * @param to           Recipient's email address
     * @param subject      Email subject
     * @param templateName Name of the Freemarker template
     * @param model        Data model for the template
     */
    public void sendHtmlMail(String to, String subject, String templateName, Map<String, Object> model) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            Template template = freemarkerConfig.getTemplate(templateName);
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (IOException | TemplateException | MessagingException | MailException e) {
            throw new RuntimeException("Failed to send html email", e);
        }
    }

    /**
     * Sends an email with an attached image inline in the HTML content.
     *
     * @param to           Recipient's email address
     * @param subject      Email subject
     * @param templateName Name of the Freemarker template
     * @param model        Data model for the template
     * @param imageFile    The image file to be attached
     */
    public void sendMailWithAttachment(String to, String subject, String templateName, Map<String, Object> model, File imageFile) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            Template template = freemarkerConfig.getTemplate(templateName);
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.addInline("imageId", new FileSystemResource(imageFile));
            mailSender.send(message);
        } catch (IOException | TemplateException | MessagingException | MailException e) {
            throw new RuntimeException("Failed to send attachment email", e);
        }
    }
}