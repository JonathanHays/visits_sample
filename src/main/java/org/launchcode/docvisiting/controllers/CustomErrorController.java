package org.launchcode.docvisiting.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest request, Model model) {
        // Get the error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // Set the error attributes for the view
            model.addAttribute("statusCode", statusCode);

            // You can add more attributes as needed

            // Render the custom error page
            return "error_404"; // Assuming you have a Thymeleaf template named "404.html" in the "error" folder
        }

        // If no error status is found, redirect to the default error page
        return "error";
    }
}
