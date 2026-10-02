package vn.edu.fpt.customer.onlinerenting.service.impl;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vn.edu.fpt.admin.carmanagement.dto.CarView;
import vn.edu.fpt.admin.carmanagement.entity.Car;
import vn.edu.fpt.admin.carmanagement.service.CarService;
import vn.edu.fpt.admin.carrentalmanagement.entity.CarRental;
import vn.edu.fpt.admin.carrentalmanagement.service.CarRentalService;
import vn.edu.fpt.admin.customermanagement.entity.Customer;
import vn.edu.fpt.admin.customermanagement.service.CustomerService;
import vn.edu.fpt.customer.onlinerenting.dto.OnlineCarRentingForm;
import vn.edu.fpt.customer.onlinerenting.service.OnlineCarRentingService;

@Service
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('CUSTOMER')")
public class OnlineCarRentingServiceImpl implements OnlineCarRentingService {

    private final CustomerService customerService;
    private final CarService carService;
    private final CarRentalService carRentalService;

    @Override
    @Transactional(readOnly = true)
    public List<CarView> getCars() {
        requireCustomer();
        return carService.getAvailableCars();
    }

    @Override
    @Transactional
    public void createRental(OnlineCarRentingForm form) {
        Customer customer = requireCustomer();
        List<Car> cars = carService.findAvailableCarsByIds(form.getSelectedCarIds());
        if (cars.size() != form.getSelectedCarIds().size()) {
            throw new IllegalArgumentException("Xe đã chọn không tồn tại hoặc không còn ở trạng thái Available.");
        }

        long rentalDays = ChronoUnit.DAYS.between(form.getPickupDate(), form.getReturnDate());
        List<CarRental> rentals = new ArrayList<>();
        for (Car car : cars) {
            BigDecimal rentPrice = car.getRentPrice().multiply(BigDecimal.valueOf(rentalDays));
            if (rentPrice.precision() - rentPrice.scale() > 10) {
                throw new IllegalArgumentException("Giá thuê vượt phạm vi lưu trữ của giao dịch thuê xe.");
            }

            CarRental rental = new CarRental();
            rental.setCustomer(customer);
            rental.setCar(car);
            rental.setPickupDate(form.getPickupDate());
            rental.setReturnDate(form.getReturnDate());
            rental.setRentPrice(rentPrice);
            rental.setStatus("Active");
            rentals.add(rental);
        }
        carRentalService.createOnlineRentals(rentals);
    }

    private Customer requireCustomer() {
        return customerService.findCurrentCustomer()
                .orElseThrow(() -> new IllegalStateException("Tài khoản chưa có hồ sơ khách hàng để thuê xe."));
    }
}
