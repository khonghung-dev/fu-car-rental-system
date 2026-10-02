package vn.edu.fpt.admin.rentalreportmanagement.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalView;
import vn.edu.fpt.admin.carrentalmanagement.service.CarRentalService;
import vn.edu.fpt.admin.rentalreportmanagement.dto.RentalReportForm;
import vn.edu.fpt.admin.rentalreportmanagement.service.RentalReportService;

@Service
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class RentalReportServiceImpl implements RentalReportService {

    private final CarRentalService carRentalService;

    @Override
    public List<CarRentalView> generateReport(RentalReportForm form) {
        return carRentalService.getRentalsForReport(form.getStartDate(), form.getEndDate());
    }
}
