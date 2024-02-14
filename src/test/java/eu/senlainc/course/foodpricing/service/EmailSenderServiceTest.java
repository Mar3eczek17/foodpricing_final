package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.UserDao;
import eu.senlainc.course.foodpricing.entities.User;
import eu.senlainc.course.foodpricing.expectations.CustomMessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private VelocityEngine velocityEngine;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private EmailSenderService emailSenderService;
    private static final String TO_EMAIL_INVALID = "invalid@example.com";
    private static final String TO_EMAIL_VALID = "valid@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendLostPasswordEmail_WithValidEmail() {
        when(userDao.findByEmail(TO_EMAIL_VALID)).thenReturn(Optional.of(new User()));

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Template mockedTemplate = mock(Template.class);
        when(velocityEngine.getTemplate(anyString())).thenReturn(mockedTemplate);

        emailSenderService.sendLostPasswordEmail(TO_EMAIL_VALID);

        verify(mailSender, times(1)).createMimeMessage();
        verify(userDao, times(1)).findByEmail(TO_EMAIL_VALID);
    }

    @Test
    void testSendLostPasswordEmail_WithInvalidEmail() {
        when(userDao.findByEmail(TO_EMAIL_INVALID)).thenReturn(Optional.empty());

        CustomMessagingException exception = assertThrows(CustomMessagingException.class,
                () -> emailSenderService.sendLostPasswordEmail(TO_EMAIL_INVALID));

        verify(mailSender, never()).createMimeMessage();
        verify(userDao, times(1)).findByEmail(TO_EMAIL_INVALID);

        assertEquals("Email address not found in the database", exception.getMessage());
    }
}