package br.com.megahack.api.controllers;


import br.com.megahack.api.business.interfaces.ICustomerBO;
import br.com.megahack.api.business.interfaces.IForgetPasswordBO;
import br.com.megahack.api.model.dto.ForgetPasswordDTO;
import br.com.megahack.api.model.dto.NewPasswordDTO;
import br.com.megahack.api.model.persistence.ForgetPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

@RestController
@RequestMapping("/forget_password")
public class ForgetPasswordController {

    @Autowired
    private ICustomerBO customerBO;

    @Autowired
    private IForgetPasswordBO forgetPasswordBO;



    //Send the email to the customer with a link to restart password
    @PostMapping
    public ResponseEntity forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO){
        ForgetPassword updated = forgetPasswordBO.saveForgetPassword(forgetPasswordDTO.getEmail());
        return ResponseEntity.notFound().build();
    }

    //Check if the token still valid
    @GetMapping("/{token}")
    public ResponseEntity<String> checkUrl(@PathVariable("token") String token){
        ForgetPassword forgetPassword = forgetPasswordBO.findByToken(token);

        //TODO : need to refactor
        Calendar yesteday = Calendar.getInstance();
        yesteday.add(Calendar.DATE, -1);

         if(forgetPassword.getCreated().after(yesteday.getTime())){
             return ResponseEntity.ok(forgetPassword.getCustomer().getUuid());
           }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    //Reset the new password
    @PostMapping("/{user_uuid}")
    public ResponseEntity newPassword(@PathVariable("user_uuid") String uuid,
                                      @RequestBody NewPasswordDTO password){
        boolean updated = customerBO.newPassword(password, uuid);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

}
