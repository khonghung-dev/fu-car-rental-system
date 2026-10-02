package vn.edu.fpt.admin.carmanagement.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vn.edu.fpt.admin.carmanagement.dto.CarForm;
import vn.edu.fpt.admin.carmanagement.dto.CarView;
import vn.edu.fpt.admin.carmanagement.entity.Car;
import vn.edu.fpt.admin.carmanagement.entity.CarProducer;
import vn.edu.fpt.admin.carmanagement.repository.CarProducerRepository;
import vn.edu.fpt.admin.carmanagement.repository.CarRepository;
import vn.edu.fpt.admin.carmanagement.service.CarService;

@Service
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarProducerRepository carProducerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CarView> getCars() {
        return carRepository.findAll().stream()
                .map(this::toView)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CarView getCar(Integer carId) {
        return toView(findCar(carId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarProducer> getProducers() {
        return carProducerRepository.findAll();
    }

    @Override
    @Transactional
    public void createCar(CarForm form) {
        Car car = new Car();
        applyForm(car, form);
        carRepository.saveAndFlush(car);
    }

    @Override
    @Transactional
    public void updateCar(Integer carId, CarForm form) {
        Car car = findCar(carId);
        applyForm(car, form);
        carRepository.saveAndFlush(car);
    }

    private Car findCar(Integer carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy xe."));
    }

    private void applyForm(Car car, CarForm form) {
        CarProducer producer = carProducerRepository.findById(form.getProducerId())
                .orElseThrow(() -> new IllegalArgumentException("Nhà sản xuất không tồn tại."));
        car.setCarName(form.getCarName());
        car.setCarModelYear(form.getCarModelYear());
        car.setColor(form.getColor());
        car.setCapacity(form.getCapacity());
        car.setDescription(form.getDescription());
        car.setImportDate(form.getImportDate());
        car.setProducer(producer);
        car.setRentPrice(form.getRentPrice());
        car.setStatus(form.getStatus());
    }

    private CarView toView(Car car) {
        CarProducer producer = car.getProducer();
        return new CarView(car.getCarId(), car.getCarName(), car.getCarModelYear(), car.getColor(),
                car.getCapacity(), car.getDescription(), car.getImportDate(), producer.getProducerId(),
                producer.getProducerName(), car.getRentPrice(), car.getStatus());
    }
}
