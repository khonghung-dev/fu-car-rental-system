package vn.edu.fpt.customerregistration.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRegistrationForm {

    @NotBlank(message = "Tên tài khoản không được để trống.")
    @Size(max = 100, message = "Tên tài khoản không được vượt quá 100 ký tự.")
    private String accountName;

    @NotBlank(message = "Email không được để trống.")
    @Size(max = 200, message = "Email không được vượt quá 200 ký tự.")
    @Email(message = "Email không đúng định dạng.")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Pattern(regexp = "(?s)\\A(?=.*\\p{javaUpperCase})(?=.*\\p{javaLowerCase})(?=.*\\p{javaDigit})(?=.*[\\p{P}\\p{S}]).*\\z",
            message = "Mật khẩu phải có chữ hoa, chữ thường, chữ số và ký tự đặc biệt.")
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống.")
    private String confirmPassword;

    @AssertTrue(message = "Mật khẩu phải có ít nhất 8 ký tự.")
    public boolean isPasswordLongEnough() {
        return password == null || password.codePointCount(0, password.length()) >= 8;
    }

    @AssertTrue(message = "Mật khẩu không được vượt quá 72 byte khi mã hóa UTF-8.")
    public boolean isPasswordWithinBcryptLimit() {
        return password == null || password.getBytes(StandardCharsets.UTF_8).length <= 72;
    }

    @AssertTrue(message = "Mật khẩu không được có khoảng trắng ở đầu hoặc cuối.")
    public boolean isPasswordWithoutBoundaryWhitespace() {
        if (password == null || password.isEmpty()) {
            return true;
        }
        int firstCharacter = password.codePointAt(0);
        int lastCharacter = password.codePointBefore(password.length());
        return !Character.isWhitespace(firstCharacter) && !Character.isSpaceChar(firstCharacter)
                && !Character.isWhitespace(lastCharacter) && !Character.isSpaceChar(lastCharacter);
    }

    @AssertTrue(message = "Xác nhận mật khẩu phải khớp chính xác với mật khẩu.")
    public boolean isPasswordsMatching() {
        return password == null || confirmPassword == null || password.equals(confirmPassword);
    }
}
