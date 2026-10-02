package vn.edu.fpt.admin.carmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "Car", schema = "dbo")
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CarID")
    private Integer carId;

    @Nationalized
    @Column(name = "CarName", nullable = false, length = 200)
    private String carName;

    @Column(name = "CarModelYear", nullable = false)
    private Integer carModelYear;

    @Nationalized
    @Column(name = "Color", nullable = false, length = 50)
    private String color;

    @Column(name = "Capacity", nullable = false)
    private Integer capacity;

    @Nationalized
    @Column(name = "Description", nullable = false, length = 1000)
    private String description;

    @Column(name = "ImportDate", nullable = false)
    private LocalDate importDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ProducerID", nullable = false)
    private CarProducer producer;

    @Column(name = "RentPrice", nullable = false, precision = 10, scale = 0)
    private BigDecimal rentPrice;

    @Nationalized
    @Column(name = "Status", nullable = false, length = 10)
    private String status;
}
