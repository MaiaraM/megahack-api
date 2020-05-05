package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.Stand;
import br.com.megahack.api.repositories.UuidRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractStandRepository<P extends Stand> extends UuidRepository<P, String> {

}
