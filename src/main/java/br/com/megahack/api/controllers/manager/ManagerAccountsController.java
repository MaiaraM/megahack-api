package br.com.megahack.api.controllers.manager;

import br.com.megahack.api.business.interfaces.IAdministratorBO;
import br.com.megahack.api.business.interfaces.ICustomerBO;
import br.com.megahack.api.model.persistence.Administrator;
import br.com.megahack.api.model.persistence.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/manager")
public class ManagerAccountsController {


    @Autowired
    private IAdministratorBO administratorBO;

    @Autowired
    private ICustomerBO customerBO;

    // This Method should create a new manager account
    @PostMapping("/accounts")
    public ResponseEntity createNewManagerAccount(@Valid  @RequestBody Administrator account){
        if(!administratorBO.createNewAdministrator(account)){ return ResponseEntity.status(HttpStatus.CONFLICT).build();}
        return ResponseEntity.created(URI.create(String.format("/manager/accounts/%s", account.getUuid()))).build();
    }

    //Just in case we decide to use different methods for creation and update
    @PutMapping("/accounts/{uuid}")
    public ResponseEntity updateManagerAccount(@PathVariable("uuid") String uuid, @Valid @RequestBody Administrator account){
        if(!administratorBO.updateAdministrator(account, uuid)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    //Should return data on a single manager account
    @GetMapping("/accounts/{find_by}")
    public ResponseEntity<Administrator> getManagerAccountById(@PathVariable("find_by") String findBy, @RequestParam(value = "by", defaultValue = "username") String searchBy){
        Administrator adm;
        if("uuid".equals(searchBy)){
            adm = administratorBO.findByUuid(findBy);
        } else {
            adm = administratorBO.findByUsername(findBy);
        }
        if (adm == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adm);
    }

    //List all manager accounts
    @GetMapping("/accounts")
    public ResponseEntity<Page> getAllManagerAccounts(Pageable pageable, @RequestParam(value = "type", defaultValue = "administrator") String searchBy){
        // First, we get the authenticated user's username
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Find the administrator account tied to it
        Administrator admin = administratorBO.findByUsername(username);

        if (admin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(searchBy.equals("administrator")) {
            Page<Administrator> admins;
            // A null Merchant means this is a superadmin
            admins = administratorBO.getAllAdministrators(pageable);
            return ResponseEntity.ok(admins);
        } else if(searchBy.equals("customers")){
            Page<Customer> customers;
            // A null Merchant means this is a superadmin
            customers = customerBO.getAllCustomers(pageable);
            return ResponseEntity.ok(customers);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/accounts/{uuid}")
    public ResponseEntity deleteManagerAccount(@PathVariable("uuid") String uuid){
        if(!administratorBO.deleteAdministrator(uuid)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
