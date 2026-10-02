package vn.edu.fpt.customer.profilemanagement.controller;

import jakarta.validation.Valid;
import java.util.Optional;
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
import vn.edu.fpt.customer.profilemanagement.dto.CustomerProfileForm;
import vn.edu.fpt.customer.profilemanagement.service.CustomerProfileService;

@Controller
@RequestMapping("/customer/profile")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final CustomerProfileService customerProfileService;

    @InitBinder("customerProfileForm")
    public void bindProfileForm(WebDataBinder binder) {
        binder.setAllowedFields("fullName", "mobile", "birthday", "identityCard", "licenceNumber",
                "licenceDate");
    }

    @GetMapping
    public String profile(Model model) {
        Optional<CustomerProfileForm> profile = customerProfileService.getProfile();
        model.addAttribute("profile", profile.orElse(null));
        model.addAttribute("profileMissing", profile.isEmpty());
        return "customer/profilemanagement/profile";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        if (customerProfileService.getProfile().isPresent()) {
            return "redirect:/customer/profile";
        }
        model.addAttribute("customerProfileForm", new CustomerProfileForm());
        model.addAttribute("creatingProfile", true);
        return "customer/profilemanagement/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("customerProfileForm") CustomerProfileForm form,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (customerProfileService.getProfile().isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tài khoản đã có hồ sơ khách hàng.");
            return "redirect:/customer/profile";
        }
        model.addAttribute("creatingProfile", true);
        if (bindingResult.hasErrors()) {
            return "customer/profilemanagement/form";
        }
        try {
            if (!customerProfileService.createProfile(form)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Tài khoản đã có hồ sơ khách hàng.");
                return "redirect:/customer/profile";
            }
        } catch (DataAccessException | TransactionException exception) {
            bindingResult.reject("profile.saveFailed", "Không thể tạo hồ sơ khách hàng. Vui lòng thử lại.");
            return "customer/profilemanagement/form";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo hồ sơ khách hàng.");
        return "redirect:/customer/profile";
    }

    @GetMapping("/edit")
    public String editForm(Model model) {
        Optional<CustomerProfileForm> profile = customerProfileService.getProfile();
        if (profile.isEmpty()) {
            return "redirect:/customer/profile";
        }
        model.addAttribute("customerProfileForm", profile.get());
        model.addAttribute("creatingProfile", false);
        return "customer/profilemanagement/form";
    }

    @PostMapping("/edit")
    public String update(@Valid @ModelAttribute("customerProfileForm") CustomerProfileForm form,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (customerProfileService.getProfile().isEmpty()) {
            return "redirect:/customer/profile";
        }
        model.addAttribute("creatingProfile", false);
        if (bindingResult.hasErrors()) {
            return "customer/profilemanagement/form";
        }
        try {
            customerProfileService.updateProfile(form);
        } catch (IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xác định hồ sơ khách hàng của tài khoản này. Vui lòng tải lại hồ sơ.");
            return "redirect:/customer/profile";
        } catch (DataAccessException | TransactionException exception) {
            bindingResult.reject("profile.saveFailed", "Không thể cập nhật hồ sơ khách hàng. Vui lòng thử lại.");
            return "customer/profilemanagement/form";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật hồ sơ khách hàng.");
        return "redirect:/customer/profile";
    }

    @ExceptionHandler({DataAccessException.class, TransactionException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String profileDataError(Model model) {
        model.addAttribute("profile", null);
        model.addAttribute("profileMissing", false);
        model.addAttribute("errorMessage", "Không thể tải hồ sơ khách hàng. Vui lòng thử lại sau.");
        return "customer/profilemanagement/profile";
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String profileStateError(Model model) {
        model.addAttribute("profile", null);
        model.addAttribute("profileMissing", false);
        model.addAttribute("errorMessage", "Không thể xác định hồ sơ khách hàng của tài khoản này.");
        return "customer/profilemanagement/profile";
    }
}
