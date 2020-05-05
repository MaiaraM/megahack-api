package br.com.megahack.api.model.dto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = ForgetPasswordDTO.class)
@Getter
@Setter
public class ForgetPasswordDTO {

    protected String email;
}
