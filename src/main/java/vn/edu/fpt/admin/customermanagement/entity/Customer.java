package vn.edu.fpt.admin.customermanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import vn.edu.fpt.auth.entity.Account;

@Entity
@Table(name = "Customer", schema = "dbo")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Integer customerId;

    @Nationalized
    @Column(name = "FullName", nullable = false, length = 200)
    private String fullName;

    @Column(name = "Mobile", nullable = false, length = 15)
    private String mobile;

    @Column(name = "Birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "IdentityCard", nullable = false, length = 20)
    private String identityCard;

    @Column(name = "LicenceNumber", nullable = false, length = 20)
    private String licenceNumber;

    @Column(name = "LicenceDate", nullable = false)
    private LocalDate licenceDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
}
