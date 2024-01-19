package org.launchcode.docvisiting;

import org.launchcode.docvisiting.models.PasswordResetToken;
import org.launchcode.docvisiting.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final String URL = "http://localhost:8080";
    private final String X_URL = "https://twitter.com/MoCorrections";
    private final String FACEBOOK_URL = "https://www.facebook.com/MissouriCorrections/";
    private final String YOUTUBE_URL = "https://www.youtube.com/user/MissouriCorrections?feature=guide\"";
    private final String SUPPORT_EMAIL = "visitation@doc.mo.gov";


    @Autowired
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }
    @Async("asyncExecutor")
    public void sendWelcomeEmail(User user) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(user.getEmail());
        helper.setSubject("Welcome to Missouri DOC Visitation Site");
        Context context = new Context();
        context.setVariable("user", user);
        setCommonContext(context);
        // Set the content type to text/html
        helper.setText(templateEngine.process("emails/visitor_welcome", context), true);

        javaMailSender.send(mimeMessage);
    }


    @Async("asyncExecutor")
    public void sendPasswordReset(PasswordResetToken passwordResetToken) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(passwordResetToken.getUser().getEmail());
        helper.setSubject("Password Reset");
        Context context = new Context();
        context.setVariable("passwordResetToken", passwordResetToken);
        context.setVariable("PASSWORD_RESET_URL",URL + "/reset/reset-password?token=" + passwordResetToken.getToken());

        setCommonContext(context);
        // Set the content type to text/html
        helper.setText(templateEngine.process("emails/password_reset", context), true);

        javaMailSender.send(mimeMessage);
    }

    @Async("asyncExecutor")
    public void sendUsername(User user) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(user.getEmail());
        helper.setSubject("Username Lookup");
        Context context = new Context();
        setCommonContext(context);
        context.setVariable("user",user);
        // Set the content type to text/html
        helper.setText(templateEngine.process("emails/username", context), true);

        javaMailSender.send(mimeMessage);
    }
    public void setCommonContext(Context context){
        context.setVariable("LOGIN_URL",URL + "/login");
        context.setVariable("X_URL",X_URL);
        context.setVariable("FACEBOOK_URL",FACEBOOK_URL);
        context.setVariable("YOUTUBE_URL",YOUTUBE_URL);
        context.setVariable("SUPPORT_EMAIL",SUPPORT_EMAIL);
    }

}
