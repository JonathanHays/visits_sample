package org.launchcode.docvisiting.controllers;
// <!-- 
// created by: Jonathan Hays
//  -->

import org.launchcode.docvisiting.components.Helpers;
import org.launchcode.docvisiting.data.LocationRepository;
import org.launchcode.docvisiting.models.Location;
import org.launchcode.docvisiting.models.User;
import org.launchcode.docvisiting.models.dto.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;


@Controller
public class HomeController {

    private static final String ADMIN_TYPE = "admin";
    private static final String STAFF_TYPE = "staff";
    private static final String VISITING_ROOM_TYPE = "visitingroom";
    private static final String STATUS_PENDING = "pending";

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private Helpers helpers;

    private static final int PAGE_SIZE = 25;

    @GetMapping("/")
    private String home(Model model, HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") Integer page) {
        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);

        switch (user.getLoginType()) {
            case ADMIN_TYPE:
                return handleAdminHomePage(model, request, pageRequest);
            case STAFF_TYPE:
                return handleStaffHomePage(model, request, user.getLocation(), pageRequest);
            case VISITING_ROOM_TYPE:
                return "redirect:/visitingroom";
            default:
                return handleVisitorHomePage(model, user, pageRequest);
        }
    }

    @PostMapping("/pendingFilter")
    private String processPendingByLocation(Model model, HttpServletRequest request,
                                            @RequestParam String location, @RequestParam(required = false, defaultValue = "0") Integer page) {
        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);

        if (user.getLoginType().equals(ADMIN_TYPE)) {
            return handleAdminPendingFilter(model, request, location, pageRequest);
        }

        return "redirect:/";
    }

    @GetMapping("/pendingFilter")
    private String processGetPendingByLocation(Model model, HttpServletRequest request,
                                               @RequestParam String locationId, @RequestParam(required = false, defaultValue = "0") Integer page) {
        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);

        if (user.getLoginType().equals(ADMIN_TYPE)) {
            return handleAdminPendingFilter(model, request, locationId, pageRequest);
        }

        return "redirect:/";
    }

    // Helper methods

    private String handleAdminHomePage(Model model, HttpServletRequest request, PageRequest pageRequest) {
        helpers.setCommonModelAttributesForUser(model, request, "Pending Visit");

        model.addAttribute("today", LocalDate.now());
        model.addAttribute("allLocations", locationRepository.findByIsActiveOrderByNameAsc(true));
        return "staff/index";
    }

    private String handleStaffHomePage(Model model, HttpServletRequest request, Location location, PageRequest pageRequest) {
        helpers.setCommonModelAttributesForUser(model, request, "Pending Visit");


        model.addAttribute("today", LocalDate.now());
        return "staff/index";
    }

    private String handleVisitorHomePage(Model model, User user,PageRequest pageRequest) {


        model.addAttribute("locationTitle", "Missouri D.O.C.");
        model.addAttribute("title", "My Visits");
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());

        return "visitor/index";
    }

    private String handleAdminPendingFilter(Model model, HttpServletRequest request, String location, PageRequest pageRequest) {
        if (location.equals("all")) {
            return handleAdminHomePage(model, request, pageRequest);
        }

        helpers.setCommonModelAttributesForUser(model, request, "Pending Visit");
        Location searchLocation = locationRepository.findById(Integer.valueOf(location)).orElse(null);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("searchLocation", searchLocation.getId());
        model.addAttribute("allLocations", locationRepository.findByIsActiveOrderByNameAsc(true));
        return "staff/index";
    }


}
