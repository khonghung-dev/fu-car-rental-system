package vn.edu.fpt.customer.profilemanagement.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import vn.edu.fpt.customer.profilemanagement.dto.CustomerProfileForm;

public interface CustomerProfileService {

    Optional<CustomerProfileForm> getProfile();

    boolean createProfile(@NotNull @Valid CustomerProfileForm form);

    void updateProfile(@NotNull @Valid CustomerProfileForm form);
}
