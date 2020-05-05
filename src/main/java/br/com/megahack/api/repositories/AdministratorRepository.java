package br.com.megahack.api.repositories;

import br.com.megahack.api.model.persistence.Administrator;
import br.com.megahack.api.repositories.abstracts.AbstractAdministratorRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends AbstractAdministratorRepository<Administrator> {

}
