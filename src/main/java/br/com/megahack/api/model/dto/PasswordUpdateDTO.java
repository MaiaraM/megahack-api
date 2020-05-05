package br.com.megahack.api.model.dto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = PasswordUpdateDTO.class)
@Getter
@Setter
public class PasswordUpdateDTO {

    protected String oldPassword;

    protected String newPassword;
}
