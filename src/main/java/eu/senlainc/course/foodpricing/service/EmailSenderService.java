package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.UserDao;
import eu.senlainc.course.foodpricing.expectations.CustomMessagingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;

@Service
public class EmailSenderService {

    @Value("${reset.password.link}")
    private String resetPasswordLink;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);
    private static final String EMAIL_SUBJECT = "Password Reset Request";
    private static final String LOST_PASSWORD_EMAIL_SENT_SUCCESS_MESSAGE = "Lost password email sent successfully to: {}";
    private static final String ERROR_SENDING_EMAIL_MESSAGE = "Error sending lost password email";
    private static final String EMAIL_NOT_FOUND_MESSAGE = "Email address not found in the database";

    public void sendLostPasswordEmail(String toEmail) {
        if (emailExists(toEmail)) {
            String lostPasswordLink = generateLostPasswordLink();

            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                Template template = velocityEngine.getTemplate("templates/lost_password_email.vm");

                VelocityContext velocityContext = new VelocityContext();
                velocityContext.put("lostPasswordLink", lostPasswordLink);

                StringWriter stringWriter = new StringWriter();
                template.merge(velocityContext, stringWriter);

                helper.setFrom("marekgrzesiak.22@gmail.com");
                helper.setTo(toEmail);
                helper.setSubject(EMAIL_SUBJECT);
                helper.setText(stringWriter.toString(), true);

                mailSender.send(message);

                logger.info(LOST_PASSWORD_EMAIL_SENT_SUCCESS_MESSAGE, toEmail);
            } catch (MessagingException e) {
                throw new CustomMessagingException(ERROR_SENDING_EMAIL_MESSAGE, e);
            }
        } else {
            throw new CustomMessagingException(EMAIL_NOT_FOUND_MESSAGE);
        }
    }

    private String generateLostPasswordLink() {
        return resetPasswordLink;
    }

    private boolean emailExists(String email) {
        return userDao.findByEmail(email).isPresent();
    }
}