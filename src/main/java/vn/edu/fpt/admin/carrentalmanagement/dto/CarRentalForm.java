package vn.edu.fpt.admin.carrentalmanagement.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class CarRentalForm {

    @NotNull(message = "Khách hàng không được để trống.")
    private Integer customerId;

    @NotNull(message = "Xe không được để trống.")
    private Integer carId;

    @NotNull(message = "Ngày nhận xe không được để trống.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate pickupDate;

    @NotNull(message = "Ngày trả xe không được để trống.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnDate;

    @NotNull(message = "Giá thuê không được để trống.")
    @Digits(integer = 10, fraction = 0,
            message = "Giá thuê phải là số nguyên có tối đa 10 chữ số, phù hợp với dữ liệu lưu trữ.")
    private BigDecimal rentPrice;

    @NotBlank(message = "Trạng thái không được để trống.")
    @Size(max = 10, message = "Trạng thái không được vượt quá 10 ký tự.")
    private String status;

    @AssertTrue(message = "Ngày trả xe phải sau ngày nhận xe.")
    public boolean isReturnDateAfterPickupDate() {
        return pickupDate == null || returnDate == null || returnDate.isAfter(pickupDate);
    }

    @AssertTrue(message = "Ngày nhận xe phải nằm trong phạm vi từ năm 1 đến năm 9999 của SQL Server.")
    public boolean isPickupDateInSqlRange() {
        return pickupDate == null || (pickupDate.getYear() >= 1 && pickupDate.getYear() <= 9999);
    }

    @AssertTrue(message = "Ngày trả xe phải nằm trong phạm vi từ năm 1 đến năm 9999 của SQL Server.")
    public boolean isReturnDateInSqlRange() {
        return returnDate == null || (returnDate.getYear() >= 1 && returnDate.getYear() <= 9999);
    }
}
