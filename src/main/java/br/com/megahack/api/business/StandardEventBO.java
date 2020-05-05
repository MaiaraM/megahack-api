package br.com.megahack.api.business;

import br.com.megahack.api.business.interfaces.IEventBO;
import br.com.megahack.api.model.persistence.CustomerType;
import br.com.megahack.api.model.persistence.Event;
import br.com.megahack.api.model.persistence.Price;
import br.com.megahack.api.repositories.abstracts.AbstractEventRepository;
import br.com.megahack.api.repositories.abstracts.AbstractPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class StandardEventBO implements IEventBO {

    @Autowired
    private AbstractEventRepository repository;

    @Autowired
    private AbstractPriceRepository priceRepository;


    @Override
    @PreAuthorize("hasPermission(#event, 'write')")

    public Event createNewEvent(Event event) {
        if (!validateEventForCreation(event)){ return null; }
        event.setSlug(getSlugForEvent(event));
        repository.saveAndFlush(event);
        return event;
    }

    @Override
    public String getSlugForEvent(Event event) {
        String slug = event.getName();
        slug = slug.replace(" ", "-").toLowerCase();
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(slug).replaceAll("");
    }



    @Override
    public Event findActiveEventByUuid(String uuid) {
        return (Event) repository.findByUuidAndActiveTrue(uuid);
    }

    @Override
    public Event findActiveEventBySlug(String slug) {
        return repository.findBySlugAndActiveTrue(slug);
    }

    @Override
    public Price findByCustomerType(String uuid, CustomerType customerType) {
        return  priceRepository.findByEventUuidAndCustomerType(uuid, customerType);
    }


    @Override
    @PreAuthorize("hasPermission(#eventUuid, 'event', 'write')")
    public Event updateEvent(Event event, String eventUuid) {
        //We want to update even non-active events
        Event updatedEvent = (Event) repository.findByUuid(eventUuid);
        if (updatedEvent == null) {throw new RuntimeException("Event not found!");}
        event.setUuid(updatedEvent.getUuid());
        repository.save(event);
        return event;
    }

    @Override
    @PreAuthorize("hasPermission(#eventUuid, 'event', 'read')")
    public Event findEventByUuid(String eventUuid) {
        return (Event) repository.findByUuid(eventUuid);
    }


    @Override
    @PreAuthorize("hasPermission(#event, 'delete')")
    public boolean deleteEvent(Event event){
        return repository.deleteByUuid(event.getUuid()) > 0;
    }

    @Override
    public Page<Event> getAllActiveEventsToDisplay(Pageable page) {
        return repository.findByActiveTrue(page);
    }

    @Override
    @PreAuthorize("hasAuthority('SUPERADMIN') ")
    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    @Override
    @PreAuthorize("hasAuthority('SUPERADMIN') ")
    public Page<Event> getAllEvents(Pageable page) {
        return repository.findAll(page);
    }



    @Override
    @PostAuthorize("hasPermission(returnObject, 'read')")
    public Event findEventBySlug(String slug) {
        return repository.findBySlug(slug);
    }

    private boolean validateEventForCreation(Event event) {
        Event databaseEvent = repository.findBySkuCode(event.getSkuCode());
        return databaseEvent == null;
    }



    @Override
    public Page<Event> getAllEventsForName(Pageable pageable, String name) {
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Event findActiveEventBuSku(String sku) {
        return repository.findBySkuCode(sku);
    }
}
