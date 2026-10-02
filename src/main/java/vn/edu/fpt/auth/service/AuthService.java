package vn.edu.fpt.auth.service;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;
import vn.edu.fpt.auth.entity.Account;

public interface AuthService extends UserDetailsService {

    Optional<Account> findAccountById(Integer accountId);

    Account getCurrentCustomerAccount();
}
