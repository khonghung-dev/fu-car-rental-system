package vn.edu.fpt.admin.carrentalmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.admin.carrentalmanagement.entity.CarRental;

public interface CarRentalRepository extends JpaRepository<CarRental, Integer> {

    List<CarRental> findByPickupDateBetweenOrderByPickupDateDesc(LocalDate startDate, LocalDate endDate);
}
