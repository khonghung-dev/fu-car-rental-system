package vn.edu.fpt.admin.carmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.admin.carmanagement.entity.CarProducer;

public interface CarProducerRepository extends JpaRepository<CarProducer, Integer> {
}
