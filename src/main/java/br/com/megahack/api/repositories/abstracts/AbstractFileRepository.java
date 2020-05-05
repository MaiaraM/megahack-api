package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.File;
import br.com.megahack.api.repositories.UuidRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractFileRepository<F extends File>  extends UuidRepository<F, String> {

}
