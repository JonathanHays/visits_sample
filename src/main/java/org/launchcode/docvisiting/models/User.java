package org.launchcode.docvisiting.models;
// <!-- 
// created by: Jonathan Hays
//  -->

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String username;
    @NotNull
    private String pwHash;
    private String firstName;
    private String lastName;
    private String email;
    private final LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean isActive;
    private String loginType;
    @ManyToOne
    private User createdBy;
    @ManyToOne
    private User modifiedBy;
    @ManyToOne
    private Location location;
    private String visitStatus;



    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User() {
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.pwHash = encoder.encode(password);
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public User(String username, String password, String firstName, String lastName, String email, String loginType) {
        this.username = username;
        this.pwHash = encoder.encode(password);
        this.createdAt = LocalDateTime.now();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.loginType = loginType;
        this.isActive = true;
    }


    public User(String username, String password, String firstName, String lastName, String email, String loginType,Location location) {
        this.username = username;
        this.pwHash = encoder.encode(password);
        this.createdAt = LocalDateTime.now();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.loginType = loginType;
        this.location = location;
        this.isActive = true;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }



    public void setPwHash(String password) {
        this.pwHash = encoder.encode(password);
    }

    public String getUsername() {
        return username;
    }

    public String loginTypeToString() {
        if (this.getLoginType().equals("admin")) {
            return "Admin";
        } else if (this.getLoginType().equals("staff")) {
            return "Staff";
        } else if (this.getLoginType().equals("visitRoom")) {
            return "Visiting Room Staff";
        } else {
            return this.getLoginType();
        }
    }

    public String getFullName() {

        String fullName = firstName + " " + lastName;

        return fullName;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
        this.modifiedAt = LocalDateTime.now();
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
        this.modifiedAt = LocalDateTime.now();
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
        this.modifiedAt = LocalDateTime.now();
    }

    public String getIsActive() {
        if (this.isActive) {
            return "Active";
        } else {
            return "Inactive";
        }
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.modifiedAt = LocalDateTime.now();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.modifiedAt = LocalDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.modifiedAt = LocalDateTime.now();
    }





    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationAbbr(){
        if(location!=null){
            return location.getAbbreviatedName();
        } else {
            return null;
        }
    }
}
