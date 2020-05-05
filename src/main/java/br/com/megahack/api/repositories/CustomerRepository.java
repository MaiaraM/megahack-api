package br.com.megahack.api.repositories;

import br.com.megahack.api.repositories.abstracts.AbstractCustomerRepository;
import br.com.megahack.api.model.persistence.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends AbstractCustomerRepository<Customer> {

}
