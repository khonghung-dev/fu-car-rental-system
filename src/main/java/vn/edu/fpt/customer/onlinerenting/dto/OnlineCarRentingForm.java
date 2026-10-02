package vn.edu.fpt.customer.onlinerenting.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class OnlineCarRentingForm {

    @NotEmpty(message = "Vui lòng chọn ít nhất một xe.")
    private List<@NotNull(message = "Mã xe không được để trống.") Integer> selectedCarIds;

    @NotNull(message = "Ngày nhận xe không được để trống.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate pickupDate;

    @NotNull(message = "Ngày trả xe không được để trống.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnDate;

    @AssertTrue(message = "Không được chọn trùng xe trong cùng một lần thuê.")
    public boolean isSelectedCarsUnique() {
        return selectedCarIds == null || new HashSet<>(selectedCarIds).size() == selectedCarIds.size();
    }

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
