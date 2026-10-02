package vn.edu.fpt.admin.carrentalmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.admin.carrentalmanagement.entity.CarRental;

public interface CarRentalRepository extends JpaRepository<CarRental, Integer> {
}
