package vn.edu.fpt.admin.customermanagement.service;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import vn.edu.fpt.admin.customermanagement.dto.CustomerForm;
import vn.edu.fpt.admin.customermanagement.dto.CustomerView;
import vn.edu.fpt.admin.customermanagement.entity.Customer;

public interface CustomerService {

    List<CustomerView> getCustomers();

    CustomerView getCustomer(Integer customerId);

    Optional<Customer> findCustomerById(Integer customerId);

    void createCustomer(@Valid CustomerForm form);

    void updateCustomer(Integer customerId, @Valid CustomerForm form);

    void deleteCustomer(Integer customerId);
}
