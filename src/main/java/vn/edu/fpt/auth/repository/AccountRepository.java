package vn.edu.fpt.auth.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.auth.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findByAccountName(String accountName);
}
