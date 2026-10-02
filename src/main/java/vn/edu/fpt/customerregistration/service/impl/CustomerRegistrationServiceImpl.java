package vn.edu.fpt.customerregistration.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vn.edu.fpt.auth.entity.Account;
import vn.edu.fpt.auth.repository.AccountRepository;
import vn.edu.fpt.customerregistration.dto.CustomerRegistrationForm;
import vn.edu.fpt.customerregistration.service.CustomerRegistrationService;

@Service
@RequiredArgsConstructor
@Validated
public class CustomerRegistrationServiceImpl implements CustomerRegistrationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean register(CustomerRegistrationForm form) {
        if (!accountRepository.findByAccountName(form.getAccountName()).isEmpty()) {
            return false;
        }

        Account account = new Account();
        account.setAccountName(form.getAccountName());
        account.setEmail(form.getEmail());
        account.setPassword(passwordEncoder.encode(form.getPassword()));
        account.setRole("Customer");
        accountRepository.saveAndFlush(account);
        return true;
    }
}
