package br.com.megahack.api.business.interfaces;

import br.com.megahack.api.model.persistence.Administrator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdministratorBO {

    Administrator findByUsername(String username);

    Administrator findByUuid(String uuid);

    boolean updateAdministrator(Administrator administrator, String uuid);

    boolean createNewAdministrator(Administrator administrator);

    boolean deleteAdministrator(String uuid);

    Page<Administrator> getAllAdministrators(Pageable page);

}
