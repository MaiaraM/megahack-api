package br.com.megahack.api.repositories;

import br.com.megahack.api.repositories.abstracts.AbstractUserRepository;
import br.com.megahack.api.model.persistence.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AbstractUserRepository<User> {
}
