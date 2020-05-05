package br.com.megahack.api.business.security;

import br.com.megahack.api.business.interfaces.IAuthorityBO;
import br.com.megahack.api.model.persistence.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.lang.reflect.ParameterizedType;

// We don't use T (and sonarQube will make sure you know that) but we need it so the getTargetClass method can do it's job
public abstract class TargetPermissionEvaluator<T> implements PermissionEvaluator {

    @Autowired
    protected IAuthorityBO authorityBO;

    public Class<?> getTargetClass(){
        return (Class) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected boolean hasAuthority(Authentication auth, Authority authority){
        for (GrantedAuthority granted : auth.getAuthorities()){
            if(granted.getAuthority().equals(authority.getName())){
                return true;
            }
        }
        return false;
    }

    public abstract String getTargetClassAsString();
}
