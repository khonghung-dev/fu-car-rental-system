package vn.edu.fpt.admin.carmanagement.service;

import jakarta.validation.Valid;
import java.util.List;
import vn.edu.fpt.admin.carmanagement.dto.CarForm;
import vn.edu.fpt.admin.carmanagement.dto.CarView;
import vn.edu.fpt.admin.carmanagement.entity.CarProducer;

public interface CarService {

    List<CarView> getCars();

    CarView getCar(Integer carId);

    List<CarProducer> getProducers();

    void createCar(@Valid CarForm form);

    void updateCar(Integer carId, @Valid CarForm form);
}
