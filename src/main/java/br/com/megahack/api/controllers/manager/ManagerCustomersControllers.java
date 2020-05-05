package br.com.megahack.api.controllers.manager;

import br.com.megahack.api.business.interfaces.ICustomerBO;
import br.com.megahack.api.model.dto.CustomerDTO;
import br.com.megahack.api.model.persistence.Customer;
import br.com.megahack.api.model.persistence.CustomerType;
import br.com.megahack.api.model.persistence.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager/customers")
@JsonView(JsonViews.ManagerView.class)
public class ManagerCustomersControllers {


    @Autowired
    protected ICustomerBO customerBO;

    @GetMapping("/{find_by}")
    public ResponseEntity<Customer> getUserByUuid(@PathVariable("find_by") String findBy,
                                                  @RequestParam(value = "by", defaultValue = "username") String searchBy) {
        Customer c;
        if("doc".equals(searchBy)){
            c = customerBO.findCustomerByDocument(findBy, false);
        }else if("uuid".equals(searchBy)){
            c = customerBO.findCustomerByUuid(findBy, false);
        }else {
            c = customerBO.findCustomerByUserUsername(findBy, false);
        }

        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @PostMapping("/types")
    public ResponseEntity<CustomerType> createNewCustomerType(@RequestBody CustomerType customerType){

        CustomerType type = customerBO.createNewCustomerType(customerType.getName());
        if (type == null) { return ResponseEntity.notFound().build(); }

        return ResponseEntity.ok().body(customerType);
    }

    @GetMapping("/types")
    public ResponseEntity<Page<Customer>> getAllCustomerType(Pageable pageable){
        Page<Customer> type = customerBO.getAllCustomerType(pageable);
        if (type == null) { return ResponseEntity.notFound().build(); }

        return ResponseEntity.ok().body(type);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity editCustomer(@PathVariable("uuid") String uuid, @RequestBody CustomerDTO customer) {
        Customer edit = customerBO.updateCustomer(customer, uuid);
        if(edit == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(edit);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity deleteCustomer(@PathVariable("uuid") String uuid){
        if(!customerBO.deleteCustomer(uuid)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
