package br.com.megahack.api.model.dto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = NewPasswordDTO.class)
@Getter
@Setter
public class NewPasswordDTO {

    protected String newPassword;
}
