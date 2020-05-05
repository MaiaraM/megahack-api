package br.com.megahack.api.repositories;

import br.com.megahack.api.model.persistence.Authority;
import br.com.megahack.api.repositories.abstracts.AbstractAuthorityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends AbstractAuthorityRepository<Authority> {


}
