package vn.edu.fpt.customer.rentalhistory.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalView;
import vn.edu.fpt.admin.carrentalmanagement.service.CarRentalService;
import vn.edu.fpt.customer.rentalhistory.service.RentalHistoryService;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class RentalHistoryServiceImpl implements RentalHistoryService {

    private final CarRentalService carRentalService;

    @Override
    public Optional<List<CarRentalView>> getRentalHistory() {
        return carRentalService.getCurrentCustomerRentals();
    }
}
