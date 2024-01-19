package org.launchcode.docvisiting.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Location name cannot be blank")
    private String name;

    @NotBlank(message = "Abbreviated name cannot be blank")
    private String abbreviatedName;

    private Boolean isActive;
    @ManyToOne
    private User createdBy;
    @ManyToOne
    private User modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    @OneToMany(mappedBy = "location")
    private final List<User> userList = new ArrayList<>();


    public Location() {
    }

    public Location(String name, String abbreviatedName) {
        this.name = name;
        this.abbreviatedName = abbreviatedName;
        this.isActive = true;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviatedName() {
        return abbreviatedName;
    }

    public void setAbbreviatedName(String abbreviatedName) {
        this.abbreviatedName = abbreviatedName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getIsActive() {
        if (this.isActive) {
            return "Active";
        } else {
            return "Inactive";
        }
    }



    public List<User> getUserList() {
        return userList;
    }


}
