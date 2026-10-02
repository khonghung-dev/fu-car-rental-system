package vn.edu.fpt.customerregistration.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import vn.edu.fpt.customerregistration.dto.CustomerRegistrationForm;

public interface CustomerRegistrationService {

    boolean register(@NotNull @Valid CustomerRegistrationForm form);
}
