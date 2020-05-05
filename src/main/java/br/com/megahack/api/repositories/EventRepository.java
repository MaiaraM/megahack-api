package br.com.megahack.api.repositories;

import br.com.megahack.api.repositories.abstracts.AbstractEventRepository;
import br.com.megahack.api.model.persistence.Event;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends AbstractEventRepository<Event> {

}
