package org.launchcode.docvisiting.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.launchcode.docvisiting.controllers.AuthenticationController;
import org.launchcode.docvisiting.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class Helpers {
    private static final Logger logger = LoggerFactory.getLogger(Helpers.class);
    private static final String ADMIN_LOGIN = "admin";
    private static final String VISITOR_LOGIN = "visitor";
    @Autowired
    private AuthenticationController authenticationController;
    public void setCommonModelAttributesForUser(Model model, HttpServletRequest request, String title) {
        try {
            HttpSession session = request.getSession();
            User user = authenticationController.getUserFromSession(session);
            // Log the information
            logger.debug("Setting common model attributes for user: {}", user);
            model.addAttribute("user",user);
            model.addAttribute("title", title);
            model.addAttribute("loginType", user.getLoginType());
            if (ADMIN_LOGIN.equals(user.getLoginType())) {
                model.addAttribute("locationTitle", "Missouri D.O.C.");
            } else if(VISITOR_LOGIN.equals(user.getLoginType())){
                model.addAttribute("locationTitle", "Missouri D.O.C.");
            }else {
                model.addAttribute("locationTitle", user.getLocation().getAbbreviatedName());
            }
        } catch (Exception e) {
            // Log the exception
            logger.error("Error while setting common model attributes", e);

            // Handle the exception or rethrow if necessary
        }
    }
}
