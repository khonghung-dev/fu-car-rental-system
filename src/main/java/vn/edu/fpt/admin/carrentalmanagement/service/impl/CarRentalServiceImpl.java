package vn.edu.fpt.admin.carrentalmanagement.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vn.edu.fpt.admin.carmanagement.dto.CarView;
import vn.edu.fpt.admin.carmanagement.entity.Car;
import vn.edu.fpt.admin.carmanagement.service.CarService;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalForm;
import vn.edu.fpt.admin.carrentalmanagement.dto.CarRentalView;
import vn.edu.fpt.admin.carrentalmanagement.entity.CarRental;
import vn.edu.fpt.admin.carrentalmanagement.repository.CarRentalRepository;
import vn.edu.fpt.admin.carrentalmanagement.service.CarRentalService;
import vn.edu.fpt.admin.customermanagement.dto.CustomerView;
import vn.edu.fpt.admin.customermanagement.entity.Customer;
import vn.edu.fpt.admin.customermanagement.service.CustomerService;

@Service
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class CarRentalServiceImpl implements CarRentalService {

    private final CarRentalRepository carRentalRepository;
    private final CustomerService customerService;
    private final CarService carService;

    @Override
    @Transactional(readOnly = true)
    public List<CarRentalView> getRentals() {
        return carRentalRepository.findAll().stream()
                .map(this::toView)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CarRentalView getRental(Integer carRenId) {
        return toView(findRental(carRenId));
    }

    @Override
    public List<CustomerView> getCustomers() {
        return customerService.getCustomers();
    }

    @Override
    public List<CarView> getCars() {
        return carService.getCars();
    }

    @Override
    @Transactional
    public void createRental(CarRentalForm form) {
        CarRental rental = new CarRental();
        applyForm(rental, form);
        carRentalRepository.saveAndFlush(rental);
    }

    @Override
    @Transactional
    public void updateRental(Integer carRenId, CarRentalForm form) {
        CarRental rental = findRental(carRenId);
        applyForm(rental, form);
        carRentalRepository.saveAndFlush(rental);
    }

    @Override
    @Transactional
    public void deleteRental(Integer carRenId) {
        CarRental rental = findRental(carRenId);
        carRentalRepository.delete(rental);
        carRentalRepository.flush();
    }

    private CarRental findRental(Integer carRenId) {
        return carRentalRepository.findById(carRenId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy giao dịch thuê xe."));
    }

    private void applyForm(CarRental rental, CarRentalForm form) {
        Customer customer = customerService.findCustomerById(form.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Khách hàng không tồn tại."));
        Car car = carService.findCarById(form.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Xe không tồn tại."));
        rental.setCustomer(customer);
        rental.setCar(car);
        rental.setPickupDate(form.getPickupDate());
        rental.setReturnDate(form.getReturnDate());
        rental.setRentPrice(form.getRentPrice());
        rental.setStatus(form.getStatus());
    }

    private CarRentalView toView(CarRental rental) {
        Customer customer = rental.getCustomer();
        Car car = rental.getCar();
        return new CarRentalView(rental.getCarRenId(), customer.getCustomerId(), customer.getFullName(),
                car.getCarId(), car.getCarName(), rental.getPickupDate(), rental.getReturnDate(),
                rental.getRentPrice(), rental.getStatus());
    }
}
