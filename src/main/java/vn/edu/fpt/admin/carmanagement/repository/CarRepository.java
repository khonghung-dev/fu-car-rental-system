package vn.edu.fpt.admin.carmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.admin.carmanagement.entity.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {
}
