package br.com.megahack.api.model.dto;

import br.com.megahack.api.model.persistence.CustomerType;
import br.com.megahack.api.model.enums.EntityType;
import br.com.megahack.api.model.enums.Gender;
import br.com.megahack.api.model.enums.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = CustomerDTO.class)
@Getter
@Setter
public class CustomerDTO {
    protected UserDTO user;

    protected String externalId;

    @NotNull
    @NotBlank(message = "firstName is mandatory")
    protected String firstName;

    protected String lastName;

    protected String nickname;

    @Enumerated(EnumType.STRING)
    protected EntityType entity;

    @Enumerated(EnumType.STRING)
    protected Gender gender = Gender.OUTROS;

    @Enumerated(EnumType.STRING)
    protected MaritalStatus maritalStatus = MaritalStatus.NA;

    protected Date birthdate;

    @NotNull
    @NotBlank(message = "document is mandatory")
    protected String document;

    protected String phone;

    protected String mobile;

    protected CustomerType customerType;

}
