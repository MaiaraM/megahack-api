package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.Customer;
import br.com.megahack.api.repositories.UuidRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractCustomerRepository<C extends Customer> extends UuidRepository<C, String>{

    @Query(value = "select c.* from customers c inner join users u on (c.user_uuid = u.uuid) where c.deleted is false and (c.document = ?1 or u.username = ?2 or u.email = ?3)" , nativeQuery = true)
    C findRegisteredCustomers(String document, String user, String email);

    C findByDocumentAndActiveTrue(String document);

    C findByUserUsernameAndActiveTrue(String username);

    C findByDocument(String document);

    C findByUserUsername(String username);

    C findByUserEmail(String email);

    Page<C> findAllByExternalIdIsNull(Pageable pageable);
}
