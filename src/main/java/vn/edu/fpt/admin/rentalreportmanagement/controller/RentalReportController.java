package vn.edu.fpt.admin.rentalreportmanagement.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.fpt.admin.rentalreportmanagement.dto.RentalReportForm;
import vn.edu.fpt.admin.rentalreportmanagement.service.RentalReportService;

@Controller
@RequestMapping("/admin/rental-report")
@RequiredArgsConstructor
public class RentalReportController {

    private final RentalReportService rentalReportService;

    @InitBinder("rentalReportForm")
    public void bindRentalReportForm(WebDataBinder binder) {
        binder.setAllowedFields("startDate", "endDate");
    }

    @GetMapping(params = {"!startDate", "!endDate"})
    public String reportForm(Model model) {
        model.addAttribute("rentalReportForm", new RentalReportForm());
        model.addAttribute("reportGenerated", false);
        return "rentalreport/report";
    }

    @GetMapping
    public String generateReport(@Valid @ModelAttribute("rentalReportForm") RentalReportForm form,
                                 BindingResult bindingResult, Model model, HttpServletResponse response) {
        model.addAttribute("reportGenerated", false);
        if (bindingResult.hasErrors()) {
            return "rentalreport/report";
        }
        try {
            model.addAttribute("rentals", rentalReportService.generateReport(form));
            model.addAttribute("reportGenerated", true);
        } catch (DataAccessException exception) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            model.addAttribute("errorMessage", "Không thể tạo báo cáo giao dịch thuê xe. Vui lòng thử lại sau.");
        }
        return "rentalreport/report";
    }
}
