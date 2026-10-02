package vn.edu.fpt.admin.carmanagement.controller;

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
import vn.edu.fpt.admin.carmanagement.dto.CarForm;
import vn.edu.fpt.admin.carmanagement.dto.CarView;
import vn.edu.fpt.admin.carmanagement.service.CarService;

@Controller
@RequestMapping("/admin/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @InitBinder("carForm")
    public void bindCarForm(WebDataBinder binder) {
        binder.setAllowedFields("carName", "carModelYear", "color", "capacity", "description",
                "importDate", "producerId", "rentPrice", "status");
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cars", carService.getCars());
        return "car/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Integer carId, Model model) {
        model.addAttribute("car", carService.getCar(carId));
        return "car/detail";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("carForm", new CarForm());
        return prepareForm(model);
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("carForm") CarForm form,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return prepareForm(model);
        }
        try {
            carService.createCar(form);
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("producerId", "producer.notFound", "Nhà sản xuất không tồn tại.");
            return prepareForm(model);
        } catch (DataIntegrityViolationException exception) {
            bindingResult.reject("car.saveFailed",
                    "Không thể lưu xe. Vui lòng kiểm tra dữ liệu và nhà sản xuất đã chọn.");
            return prepareForm(model);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo xe.");
        return "redirect:/admin/cars";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Integer carId, Model model) {
        CarView car = carService.getCar(carId);
        model.addAttribute("carId", carId);
        model.addAttribute("carForm", toForm(car));
        return prepareForm(model);
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Integer carId,
                         @Valid @ModelAttribute("carForm") CarForm form,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        carService.getCar(carId);
        model.addAttribute("carId", carId);
        if (bindingResult.hasErrors()) {
            return prepareForm(model);
        }
        try {
            carService.updateCar(carId, form);
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("producerId", "producer.notFound", "Nhà sản xuất không tồn tại.");
            return prepareForm(model);
        } catch (DataIntegrityViolationException exception) {
            bindingResult.reject("car.saveFailed",
                    "Không thể lưu xe. Vui lòng kiểm tra dữ liệu và nhà sản xuất đã chọn.");
            return prepareForm(model);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật xe.");
        return "redirect:/admin/cars";
    }

    @ExceptionHandler({NoSuchElementException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String carNotFound() {
        return "car/not-found";
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String carDataError() {
        return "car/error";
    }

    private String prepareForm(Model model) {
        model.addAttribute("producers", carService.getProducers());
        return "car/form";
    }

    private CarForm toForm(CarView car) {
        CarForm form = new CarForm();
        form.setCarName(car.getCarName());
        form.setCarModelYear(car.getCarModelYear());
        form.setColor(car.getColor());
        form.setCapacity(car.getCapacity());
        form.setDescription(car.getDescription());
        form.setImportDate(car.getImportDate());
        form.setProducerId(car.getProducerId());
        form.setRentPrice(car.getRentPrice());
        form.setStatus(car.getStatus());
        return form;
    }
}
