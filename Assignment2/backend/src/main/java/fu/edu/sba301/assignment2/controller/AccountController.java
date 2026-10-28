package fu.edu.sba301.assignment2.controller;

import fu.edu.sba301.assignment2.dto.AccountRequest;
import fu.edu.sba301.assignment2.dto.AccountResponse;
import fu.edu.sba301.assignment2.exception.BadRequestException;
import fu.edu.sba301.assignment2.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private void checkAdminRole(Integer userRole) {
        if (userRole != null && userRole != 1) {
            throw new BadRequestException("Access denied: Only Admin (AccountRole = 1) can manage accounts.");
        }
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts(
            @RequestParam(required = false) String keyword,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkAdminRole(userRole);
        return ResponseEntity.ok(accountService.searchAccounts(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Integer id,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkAdminRole(userRole);
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest request,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkAdminRole(userRole);
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Integer id,
            @Valid @RequestBody AccountRequest request,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkAdminRole(userRole);
        AccountResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Integer id,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkAdminRole(userRole);
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
