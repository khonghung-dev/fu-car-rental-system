package vn.edu.fpt.customer.profilemanagement.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vn.edu.fpt.admin.customermanagement.entity.Customer;
import vn.edu.fpt.admin.customermanagement.service.CustomerService;
import vn.edu.fpt.customer.profilemanagement.dto.CustomerProfileForm;
import vn.edu.fpt.customer.profilemanagement.service.CustomerProfileService;

@Service
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final CustomerService customerService;

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerProfileForm> getProfile() {
        return customerService.findCurrentCustomer().map(this::toForm);
    }

    @Override
    public boolean createProfile(CustomerProfileForm form) {
        return customerService.createCurrentCustomer(form);
    }

    @Override
    public void updateProfile(CustomerProfileForm form) {
        customerService.updateCurrentCustomer(form);
    }

    private CustomerProfileForm toForm(Customer customer) {
        CustomerProfileForm form = new CustomerProfileForm();
        form.setFullName(customer.getFullName());
        form.setMobile(customer.getMobile());
        form.setBirthday(customer.getBirthday());
        form.setIdentityCard(customer.getIdentityCard());
        form.setLicenceNumber(customer.getLicenceNumber());
        form.setLicenceDate(customer.getLicenceDate());
        return form;
    }
}
