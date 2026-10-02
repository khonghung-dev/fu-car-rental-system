package vn.edu.fpt.auth.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.auth.entity.Account;
import vn.edu.fpt.auth.repository.AccountRepository;
import vn.edu.fpt.auth.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Pattern BCRYPT_HASH = Pattern.compile(
            "\\A\\$2[aby]\\$(0[4-9]|[12][0-9]|3[01])\\$[./A-Za-z0-9]{53}\\z");

    private final AccountRepository accountRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Optional<Account> findAccountById(Integer accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        if (accountName == null || accountName.isBlank() || accountName.length() > 100) {
            throw new UsernameNotFoundException("Tên tài khoản hoặc mật khẩu không hợp lệ.");
        }

        List<Account> accounts = accountRepository.findByAccountName(accountName);
        if (accounts.size() != 1) {
            throw new UsernameNotFoundException("Tên tài khoản hoặc mật khẩu không hợp lệ.");
        }

        Account account = accounts.get(0);
        if (account.getPassword() == null || !BCRYPT_HASH.matcher(account.getPassword()).matches()) {
            throw new UsernameNotFoundException("Tên tài khoản hoặc mật khẩu không hợp lệ.");
        }

        String role;
        if ("Admin".equals(account.getRole())) {
            role = "ADMIN";
        } else if ("Customer".equals(account.getRole())) {
            role = "CUSTOMER";
        } else {
            throw new UsernameNotFoundException("Tên tài khoản hoặc mật khẩu không hợp lệ.");
        }

        return User.withUsername(account.getAccountName())
                .password(account.getPassword())
                .roles(role)
                .build();
    }
}
