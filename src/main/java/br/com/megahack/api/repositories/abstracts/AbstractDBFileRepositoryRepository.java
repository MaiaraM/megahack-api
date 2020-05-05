package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.DBFile;
import br.com.megahack.api.repositories.UuidRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractDBFileRepositoryRepository<F extends DBFile> extends UuidRepository<F, String>{

}
