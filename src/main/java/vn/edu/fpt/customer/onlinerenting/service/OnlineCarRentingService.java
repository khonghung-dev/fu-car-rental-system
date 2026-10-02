package vn.edu.fpt.customer.onlinerenting.service;

import jakarta.validation.Valid;
import java.util.List;
import vn.edu.fpt.admin.carmanagement.dto.CarView;
import vn.edu.fpt.customer.onlinerenting.dto.OnlineCarRentingForm;

public interface OnlineCarRentingService {

    List<CarView> getCars();

    void createRental(@Valid OnlineCarRentingForm form);
}
