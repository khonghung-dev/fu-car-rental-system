package vn.edu.fpt.customer.profilemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class CustomerProfileForm {

    @NotBlank(message = "Họ tên không được để trống.")
    @Size(max = 200, message = "Họ tên không được vượt quá 200 ký tự.")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống.")
    @Size(max = 15, message = "Số điện thoại không được vượt quá 15 ký tự.")
    private String mobile;

    @NotNull(message = "Ngày sinh không được để trống.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;

    @NotBlank(message = "Số giấy tờ tùy thân không được để trống.")
    @Size(max = 20, message = "Số giấy tờ tùy thân không được vượt quá 20 ký tự.")
    private String identityCard;

    @NotBlank(message = "Số giấy phép lái xe không được để trống.")
    @Size(max = 20, message = "Số giấy phép lái xe không được vượt quá 20 ký tự.")
    private String licenceNumber;

    @NotNull(message = "Ngày cấp giấy phép không được để trống.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate licenceDate;
}
