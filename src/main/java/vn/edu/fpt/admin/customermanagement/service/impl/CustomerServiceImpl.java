package vn.edu.fpt.admin.customermanagement.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vn.edu.fpt.auth.entity.Account;
import vn.edu.fpt.auth.service.AuthService;
import vn.edu.fpt.admin.customermanagement.dto.CustomerForm;
import vn.edu.fpt.admin.customermanagement.dto.CustomerView;
import vn.edu.fpt.admin.customermanagement.entity.Customer;
import vn.edu.fpt.admin.customermanagement.repository.CustomerRepository;
import vn.edu.fpt.admin.customermanagement.service.CustomerService;
import vn.edu.fpt.customer.profilemanagement.dto.CustomerProfileForm;

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
    @Transactional(readOnly = true)
    public Optional<Customer> findCustomerById(Integer customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    @PreAuthorize("hasRole('CUSTOMER')")
    @Transactional(readOnly = true)
    public Optional<Customer> findCurrentCustomer() {
        Account account = authService.getCurrentCustomerAccount();
        List<Customer> customers = customerRepository.findByAccountAccountId(account.getAccountId());
        if (customers.size() > 1) {
            throw new IllegalStateException("Không thể xác định hồ sơ khách hàng của tài khoản này.");
        }
        return customers.stream().findFirst();
    }

    @Override
    @PreAuthorize("hasRole('CUSTOMER')")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean createCurrentCustomer(CustomerProfileForm form) {
        Account account = authService.getCurrentCustomerAccount();
        if (!customerRepository.findByAccountAccountId(account.getAccountId()).isEmpty()) {
            return false;
        }
        Customer customer = new Customer();
        customer.setAccount(account);
        applyProfileForm(customer, form);
        customerRepository.saveAndFlush(customer);
        return true;
    }

    @Override
    @PreAuthorize("hasRole('CUSTOMER')")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateCurrentCustomer(CustomerProfileForm form) {
        Customer customer = findCurrentCustomer()
                .orElseThrow(() -> new IllegalStateException("Tài khoản chưa có hồ sơ khách hàng."));
        applyProfileForm(customer, form);
        customerRepository.saveAndFlush(customer);
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

    private void applyProfileForm(Customer customer, CustomerProfileForm form) {
        customer.setFullName(form.getFullName());
        customer.setMobile(form.getMobile());
        customer.setBirthday(form.getBirthday());
        customer.setIdentityCard(form.getIdentityCard());
        customer.setLicenceNumber(form.getLicenceNumber());
        customer.setLicenceDate(form.getLicenceDate());
    }

    private CustomerView toView(Customer customer) {
        Account account = customer.getAccount();
        return new CustomerView(customer.getCustomerId(), customer.getFullName(), customer.getMobile(),
                customer.getBirthday(), customer.getIdentityCard(), customer.getLicenceNumber(),
                customer.getLicenceDate(), account.getAccountId(), account.getAccountName());
    }
}
