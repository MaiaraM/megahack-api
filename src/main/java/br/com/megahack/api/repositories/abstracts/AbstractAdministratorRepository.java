package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.Administrator;
import br.com.megahack.api.repositories.UuidRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractAdministratorRepository<A extends Administrator> extends UuidRepository<A, String>{

    A findByUserUsername(String username);

    A findByExternalId(String code);
}
