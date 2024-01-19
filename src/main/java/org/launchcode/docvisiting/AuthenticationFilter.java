package org.launchcode.docvisiting;
// <!-- 
// created by: Jonathan Hays
//  -->
import org.launchcode.docvisiting.controllers.AuthenticationController;
import org.launchcode.docvisiting.data.UserRepository;
import org.launchcode.docvisiting.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter implements HandlerInterceptor {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> whitelist = Arrays.asList("/login", "/logout", "/css", "/register","/reset","/images","/js","/test");

    private static final List<String> adminList = Arrays.asList("/admin");

    private static final List<String> staffList = Arrays.asList(("/staff"));

    private static final List<String> visitorList = Arrays.asList("/visitor");

    private static final List<String> visitingRoomList = Arrays.asList("/visitingroom");

    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAdminPage(String path) {
        for (String pathRoot : adminList) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }


    private static boolean isStaffPageAndUserType(String path, String userType) {
        if (userType.equals("admin") || userType.equals("staff")) {
            for (String pathRoot : staffList) {
                if (path.startsWith(pathRoot)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isStaffPage(String path) {
        for (String pathRoot : staffList) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isVisitingRoomAndUserType(String path, String userType) {
        if (userType.equals("admin") || userType.equals("staff") || userType.equals("visitingroom")) {
            for (String pathRoot : visitingRoomList) {
                if (path.startsWith(pathRoot)) {
                    return true;
                }
            }
        }
        return false;
    }


    private static boolean isVisitRoom(String path) {
        for (String pathRoot : visitingRoomList) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        // Don't require sign-in for whitelisted pages
        if (isWhitelisted(request.getRequestURI())) {
            // returning true indicates that the request may proceed
            return true;
        }

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

//        //admin check
        if (isAdminPage(request.getRequestURI()) && user.getLoginType().equals("admin")) {
            return true;
        } else if (isAdminPage(request.getRequestURI()) && !user.getLoginType().equals("admin")) {
            response.sendRedirect("/");
            return true;
        }

        //staff check
        if (isStaffPageAndUserType(request.getRequestURI(), user.getLoginType())) {
            return true;
        } else if (isStaffPage(request.getRequestURI())) {
            response.sendRedirect("/");
            return true;
        }

        if (isVisitingRoomAndUserType(request.getRequestURI(), user.getLoginType())) {
            return true;
        } else if (isVisitRoom(request.getRequestURI())) {
            response.sendRedirect("/");
            return true;
        }


        // The user is logged in
        if (user != null) {
            return true;
        }

        // The user is NOT logged in
        response.sendRedirect("/login");
        return false;
    }
}
