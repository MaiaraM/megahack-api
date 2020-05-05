package br.com.megahack.api.business;

import br.com.megahack.api.model.persistence.Customer;
import br.com.megahack.api.model.persistence.ForgetPassword;
import br.com.megahack.api.repositories.abstracts.AbstractCustomerRepository;
import br.com.megahack.api.repositories.abstracts.AbstractLostPasswordRepository;
import br.com.megahack.api.business.interfaces.IForgetPasswordBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandardForgetPasswordBO implements IForgetPasswordBO {

    @Autowired
    private AbstractCustomerRepository customerRepository;

    @Autowired
    private AbstractLostPasswordRepository passwordRepository;

    @Override
    public ForgetPassword saveForgetPassword(String email) {
        Customer customerByEmail = customerRepository.findByUserEmail(email);
        if(customerByEmail != null && customerByEmail.getActive()){
            ForgetPassword forgetPassword = new ForgetPassword(customerByEmail);
            passwordRepository.save(forgetPassword);
            return forgetPassword;
        }
        return null;
    }

    @Override
    public ForgetPassword findByToken(String token) {
        return passwordRepository.findByToken(token);
    }
}
