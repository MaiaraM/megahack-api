package br.com.megahack.api.model.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = UserDTO.class)
@Getter
@Setter
public class UserDTO {
    protected String username;

    @Email
    protected String email;
}
