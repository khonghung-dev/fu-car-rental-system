package vn.edu.fpt.admin.carrentalmanagement.service;

import jakarta.validation.Valid;
import java.util.List;
import vn.edu.fpt.admin.carmanagement.dto.CarView;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalForm;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalView;
import vn.edu.fpt.admin.customermanagement.dto.CustomerView;

public interface CarRentalService {

    List<CarRentalView> getRentals();

    CarRentalView getRental(Integer carRenId);

    List<CustomerView> getCustomers();

    List<CarView> getCars();

    void createRental(@Valid CarRentalForm form);

    void updateRental(Integer carRenId, @Valid CarRentalForm form);

    void deleteRental(Integer carRenId);
}
