package br.com.megahack.api.repositories;

import br.com.megahack.api.model.persistence.DBFile;
import br.com.megahack.api.repositories.abstracts.AbstractDBFileRepositoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBFileRepository extends AbstractDBFileRepositoryRepository<DBFile> {

}
