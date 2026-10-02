package vn.edu.fpt.admin.customermanagement.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vn.edu.fpt.auth.entity.Account;
import vn.edu.fpt.auth.service.AuthService;
import vn.edu.fpt.admin.customermanagement.dto.CustomerForm;
import vn.edu.fpt.admin.customermanagement.dto.CustomerView;
import vn.edu.fpt.admin.customermanagement.entity.Customer;
import vn.edu.fpt.admin.customermanagement.repository.CustomerRepository;
import vn.edu.fpt.admin.customermanagement.service.CustomerService;

@Service
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerView> getCustomers() {
        return customerRepository.findAll().stream()
                .map(this::toView)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerView getCustomer(Integer customerId) {
        return toView(findCustomer(customerId));
    }

    @Override
    @Transactional
    public void createCustomer(CustomerForm form) {
        Customer customer = new Customer();
        applyForm(customer, form);
        customerRepository.saveAndFlush(customer);
    }

    @Override
    @Transactional
    public void updateCustomer(Integer customerId, CustomerForm form) {
        Customer customer = findCustomer(customerId);
        applyForm(customer, form);
        customerRepository.saveAndFlush(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Integer customerId) {
        Customer customer = findCustomer(customerId);
        customerRepository.delete(customer);
        customerRepository.flush();
    }

    private Customer findCustomer(Integer customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy khách hàng."));
    }

    private void applyForm(Customer customer, CustomerForm form) {
        Account account = authService.findAccountById(form.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại."));
        customer.setFullName(form.getFullName());
        customer.setMobile(form.getMobile());
        customer.setBirthday(form.getBirthday());
        customer.setIdentityCard(form.getIdentityCard());
        customer.setLicenceNumber(form.getLicenceNumber());
        customer.setLicenceDate(form.getLicenceDate());
        customer.setAccount(account);
    }

    private CustomerView toView(Customer customer) {
        Account account = customer.getAccount();
        return new CustomerView(customer.getCustomerId(), customer.getFullName(), customer.getMobile(),
                customer.getBirthday(), customer.getIdentityCard(), customer.getLicenceNumber(),
                customer.getLicenceDate(), account.getAccountId(), account.getAccountName());
    }
}
