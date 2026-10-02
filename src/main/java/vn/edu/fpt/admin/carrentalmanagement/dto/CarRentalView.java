package vn.edu.fpt.admin.carrentalmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarRentalView {

    private final Integer carRenId;
    private final Integer customerId;
    private final String customerName;
    private final Integer carId;
    private final String carName;
    private final LocalDate pickupDate;
    private final LocalDate returnDate;
    private final BigDecimal rentPrice;
    private final String status;
}
