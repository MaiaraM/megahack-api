package br.com.megahack.api.business.security;

import br.com.megahack.api.model.persistence.Customer;
import br.com.megahack.api.repositories.abstracts.AbstractCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomerPermissionEvaluator extends TargetPermissionEvaluator<Customer> {

    @Autowired
    private AbstractCustomerRepository customerRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object customer, Object permission) {
        if (hasAuthority(authentication, authorityBO.getAdminAuthority())) { return true; }
        String authenticationUuid = (String) authentication.getCredentials();
        return ((Customer) customer).getUser().getUuid().equals(authenticationUuid);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable objectKey, String objectType, Object permission) {
        Customer customer = (Customer) customerRepository.findByUuid((String)objectKey);
        return hasPermission(authentication, customer, permission);
    }

    @Override
    public String getTargetClassAsString() {
        return "customer";
    }
}
