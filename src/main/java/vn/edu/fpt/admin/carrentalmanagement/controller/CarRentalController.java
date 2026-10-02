package vn.edu.fpt.admin.carrentalmanagement.controller;

import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalForm;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalView;
import vn.edu.fpt.admin.carrentalmanagement.service.CarRentalService;

@Controller
@RequestMapping("/admin/rentals")
@RequiredArgsConstructor
public class CarRentalController {

    private final CarRentalService carRentalService;

    @InitBinder("carRentalForm")
    public void bindCarRentalForm(WebDataBinder binder) {
        binder.setAllowedFields("customerId", "carId", "pickupDate", "returnDate", "rentPrice", "status");
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rentals", carRentalService.getRentals());
        return "rental/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Integer carRenId, Model model) {
        model.addAttribute("rental", carRentalService.getRental(carRenId));
        return "rental/detail";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("carRentalForm", new CarRentalForm());
        return prepareForm(model);
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("carRentalForm") CarRentalForm form,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model);
        }
        try {
            carRentalService.createRental(form);
        } catch (IllegalArgumentException exception) {
            bindingResult.reject("rental.invalidReference", "Khách hàng hoặc xe không tồn tại. Vui lòng chọn lại.");
            return prepareForm(model);
        } catch (DataIntegrityViolationException exception) {
            bindingResult.reject("rental.saveFailed",
                    "Không thể lưu giao dịch thuê xe. Vui lòng kiểm tra dữ liệu, khách hàng và xe đã chọn.");
            return prepareForm(model);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo giao dịch thuê xe.");
        return "redirect:/admin/rentals";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Integer carRenId, Model model) {
        CarRentalView rental = carRentalService.getRental(carRenId);
        model.addAttribute("carRenId", carRenId);
        model.addAttribute("carRentalForm", toForm(rental));
        return prepareForm(model);
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Integer carRenId,
                         @Valid @ModelAttribute("carRentalForm") CarRentalForm form,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        carRentalService.getRental(carRenId);
        model.addAttribute("carRenId", carRenId);
        if (bindingResult.hasErrors()) {
            return prepareForm(model);
        }
        try {
            carRentalService.updateRental(carRenId, form);
        } catch (IllegalArgumentException exception) {
            bindingResult.reject("rental.invalidReference", "Khách hàng hoặc xe không tồn tại. Vui lòng chọn lại.");
            return prepareForm(model);
        } catch (DataIntegrityViolationException exception) {
            bindingResult.reject("rental.saveFailed",
                    "Không thể lưu giao dịch thuê xe. Vui lòng kiểm tra dữ liệu, khách hàng và xe đã chọn.");
            return prepareForm(model);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật giao dịch thuê xe.");
        return "redirect:/admin/rentals";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer carRenId, RedirectAttributes redirectAttributes) {
        try {
            carRentalService.deleteRental(carRenId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa giao dịch thuê xe.");
        } catch (DataIntegrityViolationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xóa giao dịch thuê xe vì có đánh giá hoặc dữ liệu liên quan.");
        }
        return "redirect:/admin/rentals";
    }

    @ExceptionHandler({NoSuchElementException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String rentalNotFound() {
        return "rental/not-found";
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String rentalDataError() {
        return "rental/error";
    }

    private String prepareForm(Model model) {
        model.addAttribute("customers", carRentalService.getCustomers());
        model.addAttribute("cars", carRentalService.getCars());
        return "rental/form";
    }

    private CarRentalForm toForm(CarRentalView rental) {
        CarRentalForm form = new CarRentalForm();
        form.setCustomerId(rental.getCustomerId());
        form.setCarId(rental.getCarId());
        form.setPickupDate(rental.getPickupDate());
        form.setReturnDate(rental.getReturnDate());
        form.setRentPrice(rental.getRentPrice());
        form.setStatus(rental.getStatus());
        return form;
    }
}
