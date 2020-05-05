package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractLostPasswordRepository<F extends ForgetPassword> extends JpaRepository<F, String>{
    F findByToken(String token);
}
