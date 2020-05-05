package br.com.megahack.api.repositories;

import br.com.megahack.api.model.persistence.ForgetPassword;
import br.com.megahack.api.repositories.abstracts.AbstractLostPasswordRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostPasswordRepository extends AbstractLostPasswordRepository<ForgetPassword> {
}
