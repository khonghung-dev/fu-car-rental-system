package vn.edu.fpt.admin.customermanagement.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerView {

    private final Integer customerId;
    private final String fullName;
    private final String mobile;
    private final LocalDate birthday;
    private final String identityCard;
    private final String licenceNumber;
    private final LocalDate licenceDate;
    private final Integer accountId;
    private final String accountName;
}
