package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.User;
import br.com.megahack.api.repositories.UuidRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractUserRepository<U extends User> extends UuidRepository<U, String> {

    U findByUsername(String username);
}
