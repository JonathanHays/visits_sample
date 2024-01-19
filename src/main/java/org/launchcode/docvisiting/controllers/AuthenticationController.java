package org.launchcode.docvisiting.controllers;

import org.launchcode.docvisiting.data.UserRepository;
import org.launchcode.docvisiting.models.User;
import org.launchcode.docvisiting.models.dto.LoginFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    private static final String USER_SESSION_KEY = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(USER_SESSION_KEY);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(USER_SESSION_KEY, user.getId());
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model, @RequestParam(required = false)String password,@RequestParam(required = false)String token) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        model.addAttribute("locationTitle", "Missouri D.O.C.");
        if(password != null){
            model.addAttribute("reset","Password Reset Successfully");
        }
        if(token != null){
            model.addAttribute("token","Invalid Password Reset Token. Try Forgot Password Again");
        }

        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            handleLoginError("Log In", model);
            return "login";
        }

        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());

        if (theUser == null) {
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
        } else if (!theUser.getActive()) {
            errors.rejectValue("username", "user.invalid", "The given username is inactive");
        } else if (!theUser.isMatchingPassword(loginFormDTO.getPassword())) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
        }

        if (errors.hasErrors()) {
            handleLoginError("Log In", model);
            return "login";
        }

        setUserInSession(request.getSession(), theUser);

        return "redirect:";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }

    private void handleLoginError(String title, Model model) {
        model.addAttribute("title", title);
    }
}
