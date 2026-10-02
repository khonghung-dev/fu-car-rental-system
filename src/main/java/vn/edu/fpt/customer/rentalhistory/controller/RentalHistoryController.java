package vn.edu.fpt.customer.rentalhistory.controller;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalView;
import vn.edu.fpt.customer.rentalhistory.service.RentalHistoryService;

@Controller
@RequestMapping("/customer/rental-history")
@RequiredArgsConstructor
public class RentalHistoryController {

    private final RentalHistoryService rentalHistoryService;

    @GetMapping
    public String rentalHistory(Model model) {
        Optional<List<CarRentalView>> history = rentalHistoryService.getRentalHistory();
        model.addAttribute("rentals", history.orElseGet(List::of));
        model.addAttribute("profileMissing", history.isEmpty());
        return "customer/rentalhistory/history";
    }

    @ExceptionHandler({DataAccessException.class, TransactionException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String rentalHistoryDataError(Model model) {
        model.addAttribute("rentals", List.of());
        model.addAttribute("profileMissing", false);
        model.addAttribute("errorMessage", "Không thể tải lịch sử thuê xe. Vui lòng thử lại sau.");
        return "customer/rentalhistory/history";
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String rentalHistoryStateError(Model model) {
        model.addAttribute("rentals", List.of());
        model.addAttribute("profileMissing", false);
        model.addAttribute("errorMessage", "Không thể xác định hồ sơ khách hàng của tài khoản này.");
        return "customer/rentalhistory/history";
    }
}
