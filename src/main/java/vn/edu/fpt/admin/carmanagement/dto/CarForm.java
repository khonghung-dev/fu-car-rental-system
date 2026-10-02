package vn.edu.fpt.admin.carmanagement.dto;

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
public class CarForm {

    @NotBlank(message = "Tên xe không được để trống.")
    @Size(max = 200, message = "Tên xe không được vượt quá 200 ký tự.")
    private String carName;

    @NotNull(message = "Năm sản xuất không được để trống.")
    private Integer carModelYear;

    @NotBlank(message = "Màu xe không được để trống.")
    @Size(max = 50, message = "Màu xe không được vượt quá 50 ký tự.")
    private String color;

    @NotNull(message = "Sức chứa không được để trống.")
    private Integer capacity;

    @NotBlank(message = "Mô tả không được để trống.")
    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự.")
    private String description;

    @NotNull(message = "Ngày nhập không được để trống.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate importDate;

    @NotNull(message = "Nhà sản xuất không được để trống.")
    private Integer producerId;

    @NotNull(message = "Giá thuê không được để trống.")
    @Digits(integer = 10, fraction = 0,
            message = "Giá thuê phải là số nguyên có tối đa 10 chữ số, phù hợp với dữ liệu lưu trữ.")
    private BigDecimal rentPrice;

    @NotBlank(message = "Trạng thái không được để trống.")
    @Size(max = 10, message = "Trạng thái không được vượt quá 10 ký tự.")
    private String status;

    @AssertTrue(message = "Ngày nhập phải nằm trong phạm vi từ năm 1 đến năm 9999 của SQL Server.")
    public boolean isImportDateInSqlRange() {
        return importDate == null || (importDate.getYear() >= 1 && importDate.getYear() <= 9999);
    }
}
