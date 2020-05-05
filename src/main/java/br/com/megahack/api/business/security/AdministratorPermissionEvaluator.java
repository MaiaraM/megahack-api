package br.com.megahack.api.business.security;

import br.com.megahack.api.model.persistence.Administrator;
import br.com.megahack.api.repositories.abstracts.AbstractAdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AdministratorPermissionEvaluator extends TargetPermissionEvaluator<Administrator> {

    @Autowired
    private AbstractAdministratorRepository administratorRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetObject, Object permission) {
        Administrator administrator = (Administrator) targetObject;
        String userUuid = (String) authentication.getCredentials();
        return administrator.getUser().getUuid().equals(userUuid);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable objectKey, String objectType, Object permission) {
        Administrator administrator = (Administrator) administratorRepository.findByUuid((String)objectKey);
        return hasPermission(authentication, administrator, permission);
    }

    @Override
    public String getTargetClassAsString() {
        return "administrator";
    }
}
