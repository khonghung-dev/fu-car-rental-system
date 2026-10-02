package vn.edu.fpt.admin.carmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "CarProducer", schema = "dbo")
@Getter
public class CarProducer {

    @Id
    @Column(name = "ProducerID")
    private Integer producerId;

    @Nationalized
    @Column(name = "ProducerName")
    private String producerName;
}
