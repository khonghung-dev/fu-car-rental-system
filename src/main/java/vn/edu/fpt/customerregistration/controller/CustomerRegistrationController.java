package vn.edu.fpt.customerregistration.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vn.edu.fpt.customerregistration.dto.CustomerRegistrationForm;
import vn.edu.fpt.customerregistration.service.CustomerRegistrationService;

@Controller
@RequiredArgsConstructor
public class CustomerRegistrationController {

    private final CustomerRegistrationService customerRegistrationService;

    @InitBinder("registrationForm")
    public void bindRegistrationForm(WebDataBinder binder) {
        binder.setAllowedFields("accountName", "email", "password", "confirmPassword");
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        model.addAttribute("registrationForm", new CustomerRegistrationForm());
        return "customerregistration/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationForm") CustomerRegistrationForm form,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return registrationFailure(form, bindingResult, model);
        }
        try {
            if (!customerRegistrationService.register(form)) {
                bindingResult.rejectValue("accountName", "accountName.duplicate", "Tên tài khoản đã được sử dụng.");
                return registrationFailure(form, bindingResult, model);
            }
        } catch (DataAccessException exception) {
            bindingResult.reject("registration.saveFailed", "Không thể đăng ký tài khoản. Vui lòng thử lại.");
            return registrationFailure(form, bindingResult, model);
        }
        form.setPassword(null);
        form.setConfirmPassword(null);
        return "redirect:/login?registered";
    }

    private String registrationFailure(CustomerRegistrationForm form, BindingResult bindingResult, Model model) {
        form.setPassword(null);
        form.setConfirmPassword(null);
        BindingResult safeBindingResult = new BeanPropertyBindingResult(form, bindingResult.getObjectName());
        for (ObjectError error : bindingResult.getAllErrors()) {
            if (error instanceof FieldError fieldError
                    && ("password".equals(fieldError.getField()) || "confirmPassword".equals(fieldError.getField()))) {
                safeBindingResult.addError(new FieldError(fieldError.getObjectName(), fieldError.getField(), null,
                        fieldError.isBindingFailure(), fieldError.getCodes(), fieldError.getArguments(),
                        fieldError.getDefaultMessage()));
            } else {
                safeBindingResult.addError(error);
            }
        }
        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + bindingResult.getObjectName(), safeBindingResult);
        return "customerregistration/register";
    }
}
