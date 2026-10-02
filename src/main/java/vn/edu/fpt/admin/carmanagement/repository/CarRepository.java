package vn.edu.fpt.admin.carmanagement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.admin.carmanagement.entity.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {

    List<Car> findByStatus(String status);

    List<Car> findByCarIdInAndStatus(List<Integer> carIds, String status);
}
