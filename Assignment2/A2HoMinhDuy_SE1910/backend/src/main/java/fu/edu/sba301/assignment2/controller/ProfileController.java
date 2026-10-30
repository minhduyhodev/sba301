package fu.edu.sba301.assignment2.controller;

import fu.edu.sba301.assignment2.dto.AccountResponse;
import fu.edu.sba301.assignment2.dto.ProfileUpdateRequest;
import fu.edu.sba301.assignment2.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final AccountService accountService;

    public ProfileController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getProfile(@PathVariable Integer id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateProfile(
            @PathVariable Integer id,
            @Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(accountService.updateProfile(id, request));
    }
}
