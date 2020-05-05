package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.CustomerType;
import br.com.megahack.api.repositories.UuidRepository;

public interface AbstractCustomerTypeRepository<C extends CustomerType> extends UuidRepository<C, String> {

    C findByName(String description);

    C findByExternalId(String idCustomer);
}
