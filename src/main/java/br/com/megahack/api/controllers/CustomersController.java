package br.com.megahack.api.controllers;


import br.com.megahack.api.business.interfaces.ICustomerBO;
import br.com.megahack.api.model.dto.CustomerDTO;
import br.com.megahack.api.model.dto.PasswordUpdateDTO;
import br.com.megahack.api.model.persistence.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/customers")
//Should probably rename this to Customers
public class CustomersController {

    @Autowired
    private ICustomerBO customerBO;



    @GetMapping("/{find_by}")
    public ResponseEntity<Customer> getUserByUuid(@PathVariable("find_by") String findBy,
                                                  @RequestParam(value = "by", defaultValue = "username") String searchBy) {
        Customer c;
        if("doc".equals(searchBy)){
            c = customerBO.findCustomerByDocument(findBy, true);
        }else if("uuid".equals(searchBy)){
            c = customerBO.findCustomerByUuid(findBy, true);
        }else {
            c = customerBO.findCustomerByUserUsername(findBy, true);
        }

        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    //Actually creates a new Costumer which is a specialization of User in the database
    @PostMapping
    @ResponseBody
    public ResponseEntity<Customer> createNewCustomer(@Valid @RequestBody Customer customer) {
        Customer created = customerBO.createNewCustomer(customer);
        if (created != null) {
            // TODO should I return the uuid URL or the username URL
            return ResponseEntity.created(URI.create(String.format("/customers/%s?by=uuid", customer.getUuid()))).body(created);
        }
        // 409 (CONFLICT) indicates that the request conflicts with a resource already on the server
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<CustomerDTO> editCustomer(@PathVariable("uuid") String uuid, @RequestBody CustomerDTO customer) {
        Customer edit = customerBO.updateCustomer(customer, uuid);
        if(edit == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(customer);
    }

    @PatchMapping("/{user_uuid}/password")
    @ResponseBody
    public ResponseEntity updatePassword(@PathVariable("user_uuid") String uuid, @RequestBody PasswordUpdateDTO passwordUpdateDTO){
        boolean updated = customerBO.updatePassword(passwordUpdateDTO, uuid);
        if (updated) {
            return ResponseEntity.noContent().build();
        }
        // 409 (CONFLICT) indicates that the request conflicts with a resource already on the server
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }




    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
