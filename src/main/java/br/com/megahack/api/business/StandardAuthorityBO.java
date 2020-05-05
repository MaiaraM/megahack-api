package br.com.megahack.api.business;

import br.com.megahack.api.model.persistence.Authority;
import br.com.megahack.api.repositories.abstracts.AbstractAuthorityRepository;
import br.com.megahack.api.business.interfaces.IAuthorityBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StandardAuthorityBO implements IAuthorityBO {

    @Autowired
    private AbstractAuthorityRepository authorityRepository;

    protected Authority adminsAuthority;

    protected Authority customerAuth;

    protected Authority superadminAuthority;



    @PostConstruct
    protected void loadAuthorities(){
        adminsAuthority = authorityRepository.findByName("ADMIN");
        customerAuth = authorityRepository.findByName("CUSTOMER");
        superadminAuthority = authorityRepository.findByName("SUPERADMIN");
    }

    public Authority getAdminAuthority() {
        return adminsAuthority;
    }

    public Authority getCustomerAuthority() {
        return customerAuth;
    }

    public Authority getSuperadminAuthority(){
        return superadminAuthority;
    }
}
