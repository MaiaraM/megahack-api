package br.com.megahack.api.business;

import br.com.megahack.api.business.interfaces.IAdministratorBO;
import br.com.megahack.api.business.interfaces.IAuthorityBO;
import br.com.megahack.api.model.persistence.Administrator;
import br.com.megahack.api.model.persistence.Authority;
import br.com.megahack.api.repositories.abstracts.AbstractAdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StandardAdministratorBO implements IAdministratorBO {

    @Autowired
    protected AbstractAdministratorRepository administratorRepository;

    @Autowired
    protected IAuthorityBO authorityBO;

    @Override
    @PostAuthorize("hasPermission(returnObject, 'read')")
    public Administrator findByUsername(String username) {
        return administratorRepository.findByUserUsername(username);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'read')")
    public Administrator findByUuid(String uuid) {
        return (Administrator) administratorRepository.findByUuid(uuid);
    }

    @Override
    @PreAuthorize("hasPermission(#administrator, 'write')")
    public boolean createNewAdministrator(Administrator administrator) {
        if (!validateAdministratorForCreation(administrator)) { return false; }
        Authority adminAuthority = authorityBO.getAdminAuthority();
        administrator.getUser().addAuthority(adminAuthority);
        administrator.getUser().setPassword(new BCryptPasswordEncoder().encode(administrator.getUser().getPassword()));
        administratorRepository.save(administrator);
        return true;
    }

    @Override
    @PreAuthorize("hasPermission(#administratorUuid, 'administrator', 'delete')")
    public boolean deleteAdministrator(String administratorUuid) {
        return administratorRepository.deleteByUuid(administratorUuid) > 0 ;
    }

    private boolean validateAdministratorForCreation(Administrator administrator) {
        Administrator adm = administratorRepository.findByUserUsername(administrator.getUser().getUsername());
        return adm == null;
    }

    @Override
    @PreAuthorize("hasPermission(#administrator, 'write')")
    public boolean updateAdministrator(Administrator administrator, String uuid) {
        Administrator adminDB = (Administrator) administratorRepository.findByUuid(uuid);
        if(adminDB == null) { return false; }
        adminDB.setName(administrator.getName());
        administratorRepository.save(adminDB);
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    public Page<Administrator> getAllAdministrators(Pageable page) {
        return administratorRepository.findAll(page);
    }

}
