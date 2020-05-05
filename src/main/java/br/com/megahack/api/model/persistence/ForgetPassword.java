package br.com.megahack.api.model.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "lost_password")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ForgetPassword {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerUuid")
    @NotNull
    protected Customer customer;

    @NotNull
    @Id
    protected String token = UUID.randomUUID().toString();

    @CreatedDate
    @Column(updatable = false)
    protected Date created;


    public ForgetPassword() {
    }

    public ForgetPassword(Customer customerByEmail) {
        this.customer = customerByEmail;
    }
}
