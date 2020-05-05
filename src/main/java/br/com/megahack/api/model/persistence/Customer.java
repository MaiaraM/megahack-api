package br.com.megahack.api.model.persistence;


import br.com.megahack.api.model.enums.EntityType;
import br.com.megahack.api.model.enums.Gender;
import br.com.megahack.api.model.enums.MaritalStatus;
import br.com.megahack.api.model.validation.ConditionalDocument;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="customers")
@Where(clause = "deleted=false")
@Getter
@Setter
@SQLDelete(sql = "UPDATE customers SET deleted = true WHERE uuid=?")
@ConditionalDocument(value = EntityType.FISICA, message = "Invalid document for entity FISICA")
@ConditionalDocument(value = EntityType.JURIDICA, message = "Invalid document for entity JURIDICA")
public class Customer extends Base implements ICustomerOwned {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.ManagerView.class)
    private Long idCustomer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userUuid")
    @NotNull
    @Valid
    protected User user;

    @NotNull
    @NotBlank(message = "firstName is mandatory")
    protected String firstName;

    @NotNull
    @NotBlank(message = "lastName is mandatory")
    protected String lastName;

    protected String nickname;

    @Enumerated(EnumType.STRING)
    @NotNull
    protected EntityType entity;

    @Enumerated(EnumType.STRING)
    @NotNull
    protected Gender gender = Gender.OUTROS;

    @Enumerated(EnumType.STRING)
    protected MaritalStatus maritalStatus = MaritalStatus.NA;

    protected Date birthdate;

    @NotNull
    @NotBlank(message = "document is mandatory")
    protected String document;

    protected String phone;

    protected String mobile;

    @NotNull
    protected boolean newsletter = true;

    @NotNull
    protected boolean sms = true;

    @NotNull
    protected boolean fraudster = false;


    @ManyToOne
    @JoinColumn(name = "customerTypeUuid")
    protected CustomerType customerType;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
