package br.com.megahack.api.business.interfaces;

import br.com.megahack.api.model.persistence.CustomerType;
import br.com.megahack.api.model.persistence.Event;
import br.com.megahack.api.model.persistence.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEventBO {

    Event createNewEvent(Event event);

    Event updateEvent(Event event, String uuid);

    boolean deleteEvent(Event event);

    Event findEventByUuid(String uuid);

    Event findEventBySlug(String slug);

    Price findByCustomerType(String uuid, CustomerType customerType);

    Event findActiveEventByUuid(String uuid);

    Event findActiveEventBySlug(String slug);

    Page<Event> getAllActiveEventsToDisplay(Pageable page);

    List<Event> getAllEvents();

    // This method should return *ALL* items that are not deleted
    Page<Event> getAllEvents(Pageable page);

    String getSlugForEvent(Event event);


    Page<Event> getAllEventsForName(Pageable pageable, String name);

}
