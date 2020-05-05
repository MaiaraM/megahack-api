package br.com.megahack.api.controllers.manager;


import br.com.megahack.api.business.interfaces.IAdministratorBO;
import br.com.megahack.api.business.interfaces.IEventBO;
import br.com.megahack.api.model.persistence.Administrator;
import br.com.megahack.api.model.persistence.Event;
import br.com.megahack.api.model.persistence.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/manager/events")
public class ManagerEventsController {

    @Autowired
    private IEventBO eventBO;

    @Autowired
    private IAdministratorBO administratorBO;

    @GetMapping()
    public ResponseEntity<Page<Event>> getAllEvents(Pageable pageable, @RequestParam(value = "name", required = false) String name){
        // It is the API responsibility to filter events by Merchant (if any)
        // First, we get the authenticated user's username
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Find the administrator account tied to it
        Administrator admin = administratorBO.findByUsername(username);
        Page<Event> prod;
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // A null Merchant means this is a superadmin

            if (name == null) {
                prod = eventBO.getAllEvents(pageable);
            } else {
                prod = eventBO.getAllEventsForName(pageable, name);
            }

        return ResponseEntity.ok(prod);
    }

    @PostMapping()
    @JsonView(JsonViews.ManagerView.class)
    public ResponseEntity<Event> createNewEvent(@Valid @RequestBody Event event) {
        Event newEvent = eventBO.createNewEvent(event);
            if (newEvent == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.created(URI.create(String.format("manager/events/%s", event.getSlug()))).body(event);
    }

    @JsonView(JsonViews.ManagerView.class)
    @PutMapping("/{uuid}")
    public ResponseEntity<Event> updateEvent(@PathVariable("uuid") String uuid, @Valid @RequestBody Event event) {
        Event newEvent = eventBO.updateEvent(event, uuid);
        if(newEvent == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newEvent);
    }

    @DeleteMapping("/events/{uuid}")
    @JsonView(JsonViews.ManagerView.class)
    @Transactional
    public ResponseEntity deleteEvent(@PathVariable("uuid") String uuid){
        // We do this to avoid having to fetch the event inside the BO, as the delete() method signature
        // must abide to the Aspect definition so we can have access control
        Event p = eventBO.findEventByUuid(uuid);
        // It works because java's short circuiting will prevent the function call if p == null
        if(p == null || !eventBO.deleteEvent(p)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{find_by}")
    @JsonView(JsonViews.ManagerView.class)
    public ResponseEntity<Event> getEventBySlug(@PathVariable("find_by") String findBy, @RequestParam(value = "by", defaultValue = "slug") String by) {
        Event p;
        if("uuid".equals(by)){
            p = eventBO.findEventByUuid(findBy);
        } else {
            p = eventBO.findEventBySlug(findBy);
        }

        if (p==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(p);
    }

}
