package fu.edu.sba301.assignment2.dto;

public class LoginResponse {
    private Integer accountId;
    private String accountName;
    private String accountEmail;
    private Integer accountRole; // 1 = Admin, 2 = Staff
    private String roleName;

    public LoginResponse() {
    }

    public LoginResponse(Integer accountId, String accountName, String accountEmail, Integer accountRole) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountEmail = accountEmail;
        this.accountRole = accountRole;
        this.roleName = (accountRole != null && accountRole == 1) ? "ADMIN" : "STAFF";
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public Integer getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(Integer accountRole) {
        this.accountRole = accountRole;
        this.roleName = (accountRole != null && accountRole == 1) ? "ADMIN" : "STAFF";
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
