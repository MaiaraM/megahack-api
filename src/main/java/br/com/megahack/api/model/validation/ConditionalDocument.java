package br.com.megahack.api.model.validation;

import br.com.megahack.api.model.enums.EntityType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Repeatable(Conditionals.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConditionalDocumentValidator.class})
public @interface ConditionalDocument {
    String message() default "Invalid document";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    EntityType value();
}

