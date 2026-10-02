package vn.edu.fpt.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "Account", schema = "dbo")
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    private Integer accountId;

    @Nationalized
    @Column(name = "AccountName", nullable = false, length = 100)
    private String accountName;

    @Column(name = "Email", nullable = false, length = 200)
    private String email;

    @Column(name = "Password", nullable = false, length = 200)
    private String password;

    @Nationalized
    @Column(name = "Role", nullable = false, length = 10)
    private String role;
}
