package br.com.megahack.api.repositories;

import br.com.megahack.api.repositories.abstracts.AbstractFileRepository;
import br.com.megahack.api.model.persistence.File;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends AbstractFileRepository<File> {
}
