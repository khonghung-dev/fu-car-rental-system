package vn.edu.fpt.admin.rentalreportmanagement.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalView;
import vn.edu.fpt.admin.rentalreportmanagement.dto.RentalReportForm;

public interface RentalReportService {

    List<CarRentalView> generateReport(@NotNull @Valid RentalReportForm form);
}
