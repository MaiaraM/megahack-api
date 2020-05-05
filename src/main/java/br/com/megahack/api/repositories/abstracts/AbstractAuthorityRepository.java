package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.Authority;
import br.com.megahack.api.repositories.UuidRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractAuthorityRepository<A extends Authority> extends UuidRepository<A, String> {

    A findByName(String name);

}
