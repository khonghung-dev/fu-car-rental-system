package vn.edu.fpt.customer.onlinerenting.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.customer.onlinerenting.dto.OnlineCarRentingForm;
import vn.edu.fpt.customer.onlinerenting.service.OnlineCarRentingService;

@Controller
@RequestMapping("/customer/rent")
@RequiredArgsConstructor
public class OnlineCarRentingController {

    private final OnlineCarRentingService onlineCarRentingService;

    @InitBinder("onlineCarRentingForm")
    public void bindRentingForm(WebDataBinder binder) {
        binder.setAllowedFields("selectedCarIds", "selectedCarIds[*]", "pickupDate", "returnDate");
    }

    @GetMapping
    public String rentingForm(Model model) {
        model.addAttribute("onlineCarRentingForm", new OnlineCarRentingForm());
        return prepareForm(model);
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("onlineCarRentingForm") OnlineCarRentingForm form,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model);
        }
        try {
            onlineCarRentingService.createRental(form);
        } catch (IllegalArgumentException exception) {
            bindingResult.reject("onlineRenting.invalidCars",
                    "Xe đã chọn không tồn tại, không còn ở trạng thái Available hoặc giá thuê vượt phạm vi lưu trữ. Vui lòng chọn lại xe và ngày thuê.");
            return prepareForm(model);
        } catch (IllegalStateException exception) {
            bindingResult.reject("onlineRenting.missingCustomer",
                    "Tài khoản chưa có hồ sơ khách hàng hợp lệ để thuê xe.");
            return prepareForm(model);
        } catch (DataAccessException | TransactionException exception) {
            bindingResult.reject("onlineRenting.saveFailed", "Không thể tạo giao dịch thuê xe. Vui lòng thử lại.");
            return prepareForm(model);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo giao dịch thuê xe.");
        return "redirect:/customer/rent";
    }

    @ExceptionHandler({DataAccessException.class, TransactionException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String rentingDataError(Model model) {
        model.addAttribute("errorMessage", "Không thể tải dữ liệu thuê xe. Vui lòng thử lại sau.");
        model.addAttribute("customerProfileAvailable", false);
        model.addAttribute("cars", List.of());
        return "customer/onlinerenting/rent";
    }

    private String prepareForm(Model model) {
        try {
            model.addAttribute("cars", onlineCarRentingService.getCars());
            model.addAttribute("customerProfileAvailable", true);
        } catch (IllegalStateException exception) {
            model.addAttribute("errorMessage", "Tài khoản chưa có hồ sơ khách hàng hợp lệ để thuê xe.");
            model.addAttribute("customerProfileAvailable", false);
            model.addAttribute("cars", List.of());
        }
        return "customer/onlinerenting/rent";
    }
}
