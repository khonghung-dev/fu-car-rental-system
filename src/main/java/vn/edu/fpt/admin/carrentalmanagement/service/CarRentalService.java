package vn.edu.fpt.admin.carrentalmanagement.service;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import vn.edu.fpt.admin.carmanagement.dto.CarView;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalForm;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalView;
import vn.edu.fpt.admin.carrentalmanagement.entity.CarRental;
import vn.edu.fpt.admin.customermanagement.dto.CustomerView;

public interface CarRentalService {

    List<CarRentalView> getRentals();

    Optional<List<CarRentalView>> getCurrentCustomerRentals();

    List<CarRentalView> getRentalsForReport(LocalDate startDate, LocalDate endDate);

    CarRentalView getRental(Integer carRenId);

    List<CustomerView> getCustomers();

    List<CarView> getCars();

    void createRental(@Valid CarRentalForm form);

    void createOnlineRentals(List<CarRental> rentals);

    void updateRental(Integer carRenId, @Valid CarRentalForm form);

    void deleteRental(Integer carRenId);
}
