package br.com.megahack.api.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiValidationError implements ApiSubError {

    protected String object;
    protected String field;
    protected Object rejectedValue;
    protected String message;
}
