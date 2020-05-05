package br.com.megahack.api.repositories;

import br.com.megahack.api.model.persistence.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.io.Serializable;

@NoRepositoryBean
public interface UuidRepository<V extends Base,K extends Serializable> extends JpaRepository<V, K> {

    V findByUuidAndActiveTrue(String uuid);

    V findByUuid(String uuid);

    @Modifying
    @Transactional
    int deleteByUuid(String uuid);
}
