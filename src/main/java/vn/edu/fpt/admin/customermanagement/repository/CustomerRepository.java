package vn.edu.fpt.admin.customermanagement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.admin.customermanagement.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByAccountAccountId(Integer accountId);
}
