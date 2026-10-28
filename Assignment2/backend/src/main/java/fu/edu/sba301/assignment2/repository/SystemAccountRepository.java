package fu.edu.sba301.assignment2.repository;

import fu.edu.sba301.assignment2.domain.SystemAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemAccountRepository extends JpaRepository<SystemAccount, Integer> {
    Optional<SystemAccount> findByAccountEmail(String accountEmail);
    boolean existsByAccountEmail(String accountEmail);
    List<SystemAccount> findByAccountNameContainingIgnoreCaseOrAccountEmailContainingIgnoreCase(String accountName, String accountEmail);
}
