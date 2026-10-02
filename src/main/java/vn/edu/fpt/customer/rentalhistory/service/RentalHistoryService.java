package vn.edu.fpt.customer.rentalhistory.service;

import java.util.List;
import java.util.Optional;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalView;

public interface RentalHistoryService {

    Optional<List<CarRentalView>> getRentalHistory();
}
