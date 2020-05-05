package br.com.megahack.api.model.validation;

import br.com.megahack.api.model.enums.EntityType;
import br.com.megahack.api.model.persistence.Customer;
import br.com.megahack.api.services.DocumentValidatorService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConditionalDocumentValidator implements ConstraintValidator<ConditionalDocument, Object> {

    private String message;
    private EntityType value;

    protected DocumentValidatorService documentValidatorService;

    public ConditionalDocumentValidator() {
        // We do this so hibernate can use the validator while flushing transactions
        this.documentValidatorService = new DocumentValidatorService();
    }

    @Override
    public void initialize(ConditionalDocument requiredIfChecked) {
        message = requiredIfChecked.message();
        value = requiredIfChecked.value();
    }

    @Override
    public boolean isValid(Object objectToValidate, ConstraintValidatorContext context) {
        Boolean valid = true;

        Customer c = (Customer) objectToValidate;

        if (c.getEntity()==null){
            // @NotNull annotation will throw the exception
            return true;
        }
        if (c.getDocument()==null){
            // @NotNull annotation will throw the exception
            return true;
        }

        if (c.getEntity().equals(value)) {
            if (c.getEntity().equals(EntityType.FISICA)){
                valid = documentValidatorService.validateCpf(c.getDocument());
            }else if (c.getEntity().equals(EntityType.JURIDICA)){
                valid = documentValidatorService.validateCnpj(c.getDocument());
            }
            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addPropertyNode("document").addConstraintViolation();
            }
        }
        return valid;
    }
}