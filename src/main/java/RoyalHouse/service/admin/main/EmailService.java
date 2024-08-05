package RoyalHouse.service.admin.main;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSenderImpl mailSender;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSenderImpl mailSender, OAuth2AuthorizedClientService authorizedClientService) {
        this.mailSender = mailSender;
        this.authorizedClientService = authorizedClientService;
    }

    public void sendEmailWithAttachment(String to, String subject, String text, byte[] attachmentData, String attachmentName) throws MessagingException {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("google", "374167662153-rr1ss4p9mu159tonsagh7hqromfbufj3.apps.googleusercontent.com");
        String accessToken = client.getAccessToken().getTokenValue();

        mailSender.getJavaMailProperties().put("mail.smtp.auth.mechanisms", "XOAUTH2");
        mailSender.getJavaMailProperties().put("mail.smtp.xoauth2.accessToken", accessToken);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        InputStreamSource attachmentSource = new ByteArrayResource(attachmentData);
        helper.addAttachment(attachmentName, attachmentSource);

        mailSender.send(message);
    }
}
