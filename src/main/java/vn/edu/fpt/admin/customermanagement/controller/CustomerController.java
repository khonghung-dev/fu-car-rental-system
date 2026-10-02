package vn.edu.fpt.admin.customermanagement.controller;

import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
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
import vn.edu.fpt.admin.customermanagement.dto.CustomerForm;
import vn.edu.fpt.admin.customermanagement.dto.CustomerView;
import vn.edu.fpt.admin.customermanagement.service.CustomerService;

@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @InitBinder("customerForm")
    public void bindCustomerForm(WebDataBinder binder) {
        binder.setAllowedFields("fullName", "mobile", "birthday", "identityCard", "licenceNumber",
                "licenceDate", "accountId");
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", customerService.getCustomers());
        return "customer/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Integer customerId, Model model) {
        model.addAttribute("customer", customerService.getCustomer(customerId));
        return "customer/detail";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("customerForm", new CustomerForm());
        return "customer/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("customerForm") CustomerForm form,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "customer/form";
        }
        try {
            customerService.createCustomer(form);
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("accountId", "account.notFound", "Tài khoản không tồn tại.");
            return "customer/form";
        } catch (DataIntegrityViolationException exception) {
            bindingResult.reject("customer.saveFailed",
                    "Không thể lưu khách hàng. Vui lòng kiểm tra dữ liệu và tài khoản đã chọn.");
            return "customer/form";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo khách hàng.");
        return "redirect:/admin/customers";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Integer customerId, Model model) {
        CustomerView customer = customerService.getCustomer(customerId);
        model.addAttribute("customerId", customerId);
        model.addAttribute("customerForm", toForm(customer));
        return "customer/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Integer customerId,
                         @Valid @ModelAttribute("customerForm") CustomerForm form,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        customerService.getCustomer(customerId);
        model.addAttribute("customerId", customerId);
        if (bindingResult.hasErrors()) {
            return "customer/form";
        }
        try {
            customerService.updateCustomer(customerId, form);
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("accountId", "account.notFound", "Tài khoản không tồn tại.");
            return "customer/form";
        } catch (DataIntegrityViolationException exception) {
            bindingResult.reject("customer.saveFailed",
                    "Không thể lưu khách hàng. Vui lòng kiểm tra dữ liệu và tài khoản đã chọn.");
            return "customer/form";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật khách hàng.");
        return "redirect:/admin/customers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer customerId, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(customerId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa khách hàng.");
        } catch (DataIntegrityViolationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xóa khách hàng vì có dữ liệu thuê xe liên quan.");
        }
        return "redirect:/admin/customers";
    }

    @ExceptionHandler({NoSuchElementException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String customerNotFound() {
        return "customer/not-found";
    }

    private CustomerForm toForm(CustomerView customer) {
        CustomerForm form = new CustomerForm();
        form.setFullName(customer.getFullName());
        form.setMobile(customer.getMobile());
        form.setBirthday(customer.getBirthday());
        form.setIdentityCard(customer.getIdentityCard());
        form.setLicenceNumber(customer.getLicenceNumber());
        form.setLicenceDate(customer.getLicenceDate());
        form.setAccountId(customer.getAccountId());
        return form;
    }
}
