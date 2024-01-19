package org.launchcode.docvisiting.models.dto;
// <!-- 
// created by: Jonathan Hays
//  -->

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterFormDTO extends LoginFormDTO {

    @NotNull(message = "First name cannot be blank")
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 3, message = "First name must be at least 3 letters")
    private String firstName;

    @NotNull(message = "Last name cannot be blank")
    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 3, message = "Last name must be at least 3 letters")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter valid email")
    private String email;

    private String loginType;

    private String verifyPassword;

    private String location;

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
