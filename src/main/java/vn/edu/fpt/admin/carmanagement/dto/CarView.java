package vn.edu.fpt.admin.carmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarView {

    private final Integer carId;
    private final String carName;
    private final Integer carModelYear;
    private final String color;
    private final Integer capacity;
    private final String description;
    private final LocalDate importDate;
    private final Integer producerId;
    private final String producerName;
    private final BigDecimal rentPrice;
    private final String status;
}
