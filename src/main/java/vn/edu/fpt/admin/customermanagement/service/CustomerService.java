package vn.edu.fpt.admin.customermanagement.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import vn.edu.fpt.admin.customermanagement.dto.CustomerForm;
import vn.edu.fpt.admin.customermanagement.dto.CustomerView;
import vn.edu.fpt.admin.customermanagement.entity.Customer;
import vn.edu.fpt.customer.profilemanagement.dto.CustomerProfileForm;

public interface CustomerService {

    List<CustomerView> getCustomers();

    CustomerView getCustomer(Integer customerId);

    Optional<Customer> findCustomerById(Integer customerId);

    Optional<Customer> findCurrentCustomer();

    boolean createCurrentCustomer(@NotNull @Valid CustomerProfileForm form);

    void updateCurrentCustomer(@NotNull @Valid CustomerProfileForm form);

    void createCustomer(@Valid CustomerForm form);

    void updateCustomer(Integer customerId, @Valid CustomerForm form);

    void deleteCustomer(Integer customerId);
}
