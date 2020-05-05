package br.com.megahack.api.business.security;

import br.com.megahack.api.business.interfaces.IAuthorityBO;
import br.com.megahack.api.model.persistence.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class GlobalPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    protected List<TargetPermissionEvaluator> permissionEvaluators;

    @Autowired
    protected IAuthorityBO authorityBO;

    @Override
    public boolean hasPermission(Authentication authentication, Object target, Object permission) {
        // SuperAdmin may always access everything and accessing null objects is always allowed
        if(hasAuthority(authentication, authorityBO.getSuperadminAuthority()) ||
                target == null) {return true;}

        for (TargetPermissionEvaluator evaluator : permissionEvaluators) {
            if(evaluator.getTargetClass().isAssignableFrom(target.getClass())){
                return evaluator.hasPermission(authentication, target, permission);
            }
        }

        throw new UnsupportedOperationException("Missing Permission Evaluator for Class");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable objectKey, String objectType, Object permission) {
       // SuperAdmin may always access everything
        if(hasAuthority(authentication, authorityBO.getSuperadminAuthority())) {return true;}


        for (TargetPermissionEvaluator evaluator : permissionEvaluators) {
            if(evaluator.getTargetClassAsString().equalsIgnoreCase(objectType)){
                return evaluator.hasPermission(authentication, objectKey, objectType, permission);
            }
        }

        throw new UnsupportedOperationException("Missing Permission Evaluator for Class");
    }

    protected boolean hasAuthority(Authentication auth, Authority authority){
        for (GrantedAuthority granted : auth.getAuthorities()){
            if(granted.getAuthority().equals(authority.getName())){
                return true;
            }
        }
        return false;
    }
}
