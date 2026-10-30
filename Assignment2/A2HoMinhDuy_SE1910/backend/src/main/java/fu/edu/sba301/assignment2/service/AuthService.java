package fu.edu.sba301.assignment2.service;

import fu.edu.sba301.assignment2.domain.SystemAccount;
import fu.edu.sba301.assignment2.dto.LoginRequest;
import fu.edu.sba301.assignment2.dto.LoginResponse;
import fu.edu.sba301.assignment2.exception.BadRequestException;
import fu.edu.sba301.assignment2.repository.SystemAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SystemAccountRepository accountRepository;

    public AuthService(SystemAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public LoginResponse login(LoginRequest request) {
        SystemAccount account = accountRepository.findByAccountEmail(request.getEmail().trim())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!account.getAccountPassword().equals(request.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return new LoginResponse(
                account.getAccountId(),
                account.getAccountName(),
                account.getAccountEmail(),
                account.getAccountRole()
        );
    }
}
