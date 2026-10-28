package fu.edu.sba301.assignment2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileUpdateRequest {

    @NotBlank(message = "Account name is required")
    @Size(max = 100, message = "Account name cannot exceed 100 characters")
    private String accountName;

    @Size(max = 100, message = "Password cannot exceed 100 characters")
    private String newPassword;

    public ProfileUpdateRequest() {
    }

    public ProfileUpdateRequest(String accountName, String newPassword) {
        this.accountName = accountName;
        this.newPassword = newPassword;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
