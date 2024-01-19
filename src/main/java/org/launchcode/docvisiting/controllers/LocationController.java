package org.launchcode.docvisiting.controllers;

import org.launchcode.docvisiting.components.Helpers;
import org.launchcode.docvisiting.data.LocationRepository;
import org.launchcode.docvisiting.models.Location;
import org.launchcode.docvisiting.models.User;
import org.launchcode.docvisiting.models.dto.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin()
@Controller
@RequestMapping("/admin/locations")
public class LocationController {

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private Helpers helpers;

    private static final int PAGE_SIZE = 25;

    private static final Sort sort = Sort.by("name").ascending();

    // Load locations view
    @GetMapping
    private String loadLocationsView(Model model, HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") Integer page) {
        helpers.setCommonModelAttributesForUser(model, request, "Location Options");
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Location> results = locationRepository.findAll(pageRequest.withSort(sort));
        model.addAttribute("items", new PaginationResponse(results, page));
        return "locations/view";
    }

    // Create a new location
    @GetMapping("create")
    private String createLocation(Model model, HttpServletRequest request) {
        helpers.setCommonModelAttributesForUser(model, request, "Create Location");
        model.addAttribute(new Location());
        return "locations/create";
    }

    // Process create new location
    @PostMapping("create")
    private String processCreateLocationForm(Model model, HttpServletRequest request, @ModelAttribute @Valid Location location, Errors errors) {
        if (errors.hasErrors()) {
            helpers.setCommonModelAttributesForUser(model, request, "Create Location");
            return "locations/create";
        }
        saveLocation(request, location);
        return "redirect:/admin/locations";
    }

    // Load results of location search
    @PostMapping("results")
    private String processLocationSearch(Model model, HttpServletRequest request, @RequestParam String searchTerm, @RequestParam(required = false, defaultValue = "0") Integer page) {
        helpers.setCommonModelAttributesForUser(model, request, "Location Options");
        model.addAttribute("title", "Location Options");
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Location> results = locationRepository.findByNameContainingOrAbbreviatedNameContainingIgnoreCaseOrderByNameAsc(searchTerm, searchTerm, pageRequest.withSort(sort));
        setPaginationResponse(model, results, page);
        model.addAttribute("searchTerm", searchTerm);
        return "locations/view";
    }

    @GetMapping("results")
    private String processLocationSearchLink(Model model, HttpServletRequest request, @RequestParam String searchTerm, @RequestParam(required = false, defaultValue = "0") Integer page) {
        helpers.setCommonModelAttributesForUser(model, request, "Location Options");
        model.addAttribute("title", "Location Options");
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Location> results = locationRepository.findByNameContainingOrAbbreviatedNameContainingIgnoreCaseOrderByNameAsc(searchTerm, searchTerm, pageRequest.withSort(sort));
        setPaginationResponse(model, results, page);
        model.addAttribute("searchTerm", searchTerm);
        return "locations/view";
    }

    // Process edit location
    @GetMapping("edit/{id}")
    private String locationEditForm(Model model, HttpServletRequest request, @PathVariable int id) {
        helpers.setCommonModelAttributesForUser(model, request, "Location Details");
        Optional<Location> result = locationRepository.findById(id);
        Location location = result.orElseThrow(); // using orElseThrow instead of get
        model.addAttribute("location", location);
        return "locations/edit";
    }

    @PostMapping("edit/{id}")
    private String processLocationEditForm(Model model, HttpServletRequest request, @RequestParam int id,
                                           @RequestParam String name, @RequestParam String abbreviatedName, @RequestParam boolean isActive) {

        Optional<Location> result = locationRepository.findById(id);
        Location location = result.orElseThrow(); // using orElseThrow instead of get
        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        updateLocationFields(location, user, name, abbreviatedName, isActive);
        locationRepository.save(location);
        return "redirect:/admin/locations";
    }

    // Helper methods...

    private void saveLocation(HttpServletRequest request, Location location) {
        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        location.setCreatedBy(user);
        location.setCreatedAt(LocalDateTime.now());
        location.setActive(true);
        locationRepository.save(location);
    }

    private void setPaginationResponse(Model model, Page<Location> results, Integer page) {
        model.addAttribute("items", new PaginationResponse(results, page));
    }

    private void updateLocationFields(Location location, User user, String name, String abbreviatedName, boolean isActive) {
        location.setModifiedBy(user);
        location.setModifiedAt(LocalDateTime.now());
        location.setName(name);
        location.setAbbreviatedName(abbreviatedName);
        location.setActive(isActive);
    }
}
