package org.launchcode.docvisiting.controllers;

import org.launchcode.docvisiting.components.Helpers;
import org.launchcode.docvisiting.data.LocationRepository;
import org.launchcode.docvisiting.data.UserRepository;
import org.launchcode.docvisiting.models.Location;
import org.launchcode.docvisiting.models.User;
import org.launchcode.docvisiting.models.dto.PaginationResponse;
import org.launchcode.docvisiting.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin")
public class StaffController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private Helpers helpers;

    private static final int PAGE_SIZE = 25;
    private static final String visitorType = "visitor";
    private static final Sort sort = Sort.by("lastName").ascending();

    @GetMapping(value = "/users")
    public String displayStaff(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(required = false, defaultValue = "0") Integer page) {
        helpers.setCommonModelAttributesForUser(model, request, "Staff Options");
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<User> results = userRepository.findByLoginTypeNot(visitorType, pageRequest.withSort(sort));
        model.addAttribute("items", new PaginationResponse(results, page));

        return "staff/view";
    }

    @PostMapping(value = "/users/results")
    public String displaySearchStaffResults(Model model, @RequestParam String searchTerm, HttpServletRequest request,@RequestParam(required = false, defaultValue = "0") Integer page) {
        helpers.setCommonModelAttributesForUser(model, request, "Staff Options");
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<User> results;
        if(searchTerm == null || searchTerm.isEmpty()){
            results = userRepository.findByLoginTypeNot(visitorType, pageRequest.withSort(sort));
        } else {
            results = userRepository.findByLoginTypeNotAndLastNameContainingOrFirstNameContainingOrEmailContainingIgnoreCaseOrderByFirstNameAsc(visitorType, searchTerm,pageRequest);
        }
        model.addAttribute("items", new PaginationResponse(results, page));
        model.addAttribute("searchTerm",searchTerm);
        return "staff/view";
    }

    @GetMapping(value = "/users/results")
    public String displaySearchStaffResultsGet(Model model, @RequestParam String searchTerm, HttpServletRequest request,@RequestParam(required = false, defaultValue = "0") Integer page) {
        helpers.setCommonModelAttributesForUser(model, request, "Staff Options");
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<User> results = userRepository.findByLoginTypeNotAndLastNameContainingOrFirstNameContainingOrEmailContainingIgnoreCaseOrderByFirstNameAsc(visitorType, searchTerm,pageRequest);
        model.addAttribute("items", new PaginationResponse(results, page));
        model.addAttribute("searchTerm",searchTerm);
        return "staff/view";
    }

    @GetMapping(value = "/users/create")
    public String loadCreateForm(Model model, HttpServletRequest request) {
        setupCreateFormModel(model, request);
        model.addAttribute(new RegisterFormDTO());
        return "staff/create";
    }

    @PostMapping(value = "/users/create")
    public String processCreateNewStaff(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                        Errors errors, HttpServletRequest request,
                                        Model model) {

        if (errors.hasErrors()) {
            setupCreateFormModel(model, request);
            return "staff/create";
        }

        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            setupCreateFormModel(model, request);
            return "staff/create";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            setupCreateFormModel(model, request);
            return "staff/create";
        }

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword(), registerFormDTO.getFirstName(), registerFormDTO.getLastName(), registerFormDTO.getEmail(), registerFormDTO.getLoginType());
        newUser.setActive(true);
        newUser.setLoginType(registerFormDTO.getLoginType());
        newUser.setCreatedBy(user);

        if (!registerFormDTO.getLocation().isEmpty()) {
            Optional<Location> result = locationRepository.findById(Integer.valueOf(registerFormDTO.getLocation()));
            result.ifPresent(newUser::setLocation);
        }

        userRepository.save(newUser);

        return "redirect:/admin/users";
    }

    private void setupCreateFormModel(Model model, HttpServletRequest request) {
        helpers.setCommonModelAttributesForUser(model, request, "Create new staff");
        model.addAttribute("allLocations", locationRepository.findByIsActiveOrderByNameAsc(true));
    }

    @GetMapping(value = "/users/details/{id}")
    public String displayStaffDetails(Model model, @PathVariable int id, HttpServletRequest request) {
        helpers.setCommonModelAttributesForUser(model, request, "Create new staff");
        Optional<User> results = userRepository.findById(id);
        if (results.isPresent()) {
            User staff = results.get();
            model.addAttribute("staff", staff);
            model.addAttribute("allLocations", locationRepository.findByIsActiveOrderByNameAsc(true));
            return "staff/edit";
        } else {
            // Handle the case where the user with the given ID is not found
            return "redirect:/admin/users";
        }
    }

    @PostMapping(value = "/users/edit")
    public String processEditStaff(@RequestParam int id, @RequestParam String firstName,
                                   @RequestParam String lastName, @RequestParam String email,
                                   @RequestParam Boolean isActive, @RequestParam String loginType, @RequestParam(required = false) Location location,
                                   HttpServletRequest request,
                                   Model model) {

        Optional<User> results = userRepository.findById(id);
        if (results.isPresent()) {
            User editUser = results.get();
            editUser.setEmail(email);
            editUser.setLastName(lastName);
            editUser.setFirstName(firstName);
            editUser.setActive(isActive);
            editUser.setLocation(location);
            HttpSession session = request.getSession();
            User user = authenticationController.getUserFromSession(session);
            editUser.setModifiedBy(user);
            editUser.setLoginType(loginType);
            userRepository.save(editUser);
        }

        return "redirect:/admin/users";
    }

}
