package fu.edu.sba301.assignment2.service;

import fu.edu.sba301.assignment2.domain.SystemAccount;
import fu.edu.sba301.assignment2.dto.AccountRequest;
import fu.edu.sba301.assignment2.dto.AccountResponse;
import fu.edu.sba301.assignment2.dto.ProfileUpdateRequest;
import fu.edu.sba301.assignment2.exception.BadRequestException;
import fu.edu.sba301.assignment2.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment2.repository.NewsArticleRepository;
import fu.edu.sba301.assignment2.repository.SystemAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final SystemAccountRepository accountRepository;
    private final NewsArticleRepository newsArticleRepository;

    public AccountService(SystemAccountRepository accountRepository, NewsArticleRepository newsArticleRepository) {
        this.accountRepository = accountRepository;
        this.newsArticleRepository = newsArticleRepository;
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AccountResponse> searchAccounts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAccounts();
        }
        String term = keyword.trim();
        return accountRepository.findByAccountNameContainingIgnoreCaseOrAccountEmailContainingIgnoreCase(term, term).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AccountResponse getAccountById(Integer id) {
        SystemAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return toResponse(account);
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        if (accountRepository.existsByAccountEmail(request.getAccountEmail().trim())) {
            throw new BadRequestException("Email already exists: " + request.getAccountEmail());
        }

        if (request.getAccountPassword() == null || request.getAccountPassword().trim().isEmpty()) {
            throw new BadRequestException("Password cannot be empty");
        }

        SystemAccount account = new SystemAccount();
        account.setAccountName(request.getAccountName().trim());
        account.setAccountEmail(request.getAccountEmail().trim());
        account.setAccountRole(request.getAccountRole());
        account.setAccountPassword(request.getAccountPassword().trim());

        SystemAccount saved = accountRepository.save(account);
        return toResponse(saved);
    }

    @Transactional
    public AccountResponse updateAccount(Integer id, AccountRequest request) {
        SystemAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        String newEmail = request.getAccountEmail().trim();
        if (!account.getAccountEmail().equalsIgnoreCase(newEmail) && accountRepository.existsByAccountEmail(newEmail)) {
            throw new BadRequestException("Email already exists: " + newEmail);
        }

        account.setAccountName(request.getAccountName().trim());
        account.setAccountEmail(newEmail);
        account.setAccountRole(request.getAccountRole());

        // Update password only if provided
        if (request.getAccountPassword() != null && !request.getAccountPassword().trim().isEmpty()) {
            account.setAccountPassword(request.getAccountPassword().trim());
        }

        SystemAccount updated = accountRepository.save(account);
        return toResponse(updated);
    }

    @Transactional
    public void deleteAccount(Integer id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }

        // Rule: The delete action will delete an account in the case this account is not belong to any news articles (created).
        // If the account is already created any news article, cannot delete.
        if (newsArticleRepository.existsByCreatedBy_AccountId(id)) {
            throw new BadRequestException("Cannot delete account because it has already created news articles.");
        }

        accountRepository.deleteById(id);
    }

    @Transactional
    public AccountResponse updateProfile(Integer id, ProfileUpdateRequest request) {
        SystemAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        account.setAccountName(request.getAccountName().trim());
        if (request.getNewPassword() != null && !request.getNewPassword().trim().isEmpty()) {
            account.setAccountPassword(request.getNewPassword().trim());
        }

        SystemAccount saved = accountRepository.save(account);
        return toResponse(saved);
    }

    public AccountResponse toResponse(SystemAccount account) {
        return new AccountResponse(
                account.getAccountId(),
                account.getAccountName(),
                account.getAccountEmail(),
                account.getAccountRole()
        );
    }
}
