package vn.edu.fpt.admin.rentalreportmanagement.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class RentalReportForm {

    @NotNull(message = "Ngày bắt đầu không được để trống.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @AssertTrue(message = "Ngày bắt đầu không được sau ngày kết thúc.")
    public boolean isValidPeriod() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }

    @AssertTrue(message = "Ngày bắt đầu phải nằm trong phạm vi từ năm 1 đến năm 9999 của SQL Server.")
    public boolean isStartDateInSqlRange() {
        return startDate == null || (startDate.getYear() >= 1 && startDate.getYear() <= 9999);
    }

    @AssertTrue(message = "Ngày kết thúc phải nằm trong phạm vi từ năm 1 đến năm 9999 của SQL Server.")
    public boolean isEndDateInSqlRange() {
        return endDate == null || (endDate.getYear() >= 1 && endDate.getYear() <= 9999);
    }
}
