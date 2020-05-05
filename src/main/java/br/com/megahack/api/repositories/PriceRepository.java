package br.com.megahack.api.repositories;

import br.com.megahack.api.repositories.abstracts.AbstractPriceRepository;
import br.com.megahack.api.model.persistence.Price;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends AbstractPriceRepository<Price> {

}
