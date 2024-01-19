package org.launchcode.docvisiting.controllers;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.launchcode.docvisiting.EmailService;
import org.launchcode.docvisiting.PasswordResetTokenService;
import org.launchcode.docvisiting.data.PasswordResetTokenRepository;
import org.launchcode.docvisiting.data.UserRepository;
import org.launchcode.docvisiting.models.PasswordResetToken;
import org.launchcode.docvisiting.models.User;
import org.launchcode.docvisiting.models.dto.PasswordResetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Controller
@RequestMapping(value = "/reset")
public class PasswordResetController {
    private final EmailService emailService;
    @Autowired
    private PasswordResetTokenService tokenService;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    public PasswordResetController(EmailService emailService) {
        this.emailService = emailService;
    }
    @GetMapping("/forgot-username")
    public String showForgotUsernameForm(Model model){
        model.addAttribute("title","Forgot Username");
        model.addAttribute("email",null);
        return "reset/forgot_username";
    }

    @PostMapping("/forgot-username")
    public String processForgotUsernameForm(Model model,String email) throws MessagingException {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null){
            model.addAttribute("title","Forgot Username");
            model.addAttribute("email",email);
            model.addAttribute("error","No user found with this email address");
            return "reset/forgot_username";
        }
        emailService.sendUsername(user);
        return "reset/success";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model){
        model.addAttribute("title","Forgot Password");
        model.addAttribute("email",null);
        return "reset/forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(Model model, String email) throws MessagingException {
        User user = userRepository.findByEmailIgnoreCase(email);

        if (user == null){
            model.addAttribute("title","Forgot Password");
            model.addAttribute("email",email);
            model.addAttribute("error","No user found with this email address");
            return "reset/forgot_password";
        }
        PasswordResetToken existingToken = tokenRepository.findByUser(user);

        if (existingToken != null){
            tokenService.deleteToken(existingToken);
        }

        String token = tokenService.generateUniqueToken();
        PasswordResetToken passwordResetToken = tokenService.createToken(user,token,30);
        emailService.sendPasswordReset(passwordResetToken);
        return "reset/success";
    }
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = tokenService.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().before(new Date())) {
            // Token is invalid or expired
            return "redirect:/login?token=invalid";
        }

        // Allow the user to reset the password
        model.addAttribute("title","Password Reset");
        model.addAttribute("token", token);
        model.addAttribute(new PasswordResetDTO());
        return "reset/reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @ModelAttribute @Valid PasswordResetDTO passwordResetDTO, Errors errors, Model model) {
        System.out.println("TESTING");
        if (errors.hasErrors()) {
            model.addAttribute("token", token);
            model.addAttribute("title","Password Reset");
            return "reset/reset_password";
        }

        PasswordResetToken resetToken = tokenService.findByToken(token);

        if (resetToken == null || resetToken.getExpiryDate().before(new Date())) {
            // Token is invalid or expired
            return "redirect:/login?token=invalid";
        }

        String password = passwordResetDTO.getPassword();
        String verifyPassword = passwordResetDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title","Password Reset");
            return "reset/reset_password";
        }

        // Update the user's password and invalidate the token
        User user = resetToken.getUser();
        user.setPwHash(passwordResetDTO.getPassword());
        userRepository.save(user);
        tokenService.deleteToken(resetToken);

        return "redirect:/login?password=reset";
    }
}
