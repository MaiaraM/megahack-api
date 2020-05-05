package br.com.megahack.api.controllers.manager;


import br.com.megahack.api.business.interfaces.IAdministratorBO;
import br.com.megahack.api.business.interfaces.IEventBO;
import br.com.megahack.api.model.persistence.Administrator;
import br.com.megahack.api.model.persistence.Event;
import br.com.megahack.api.model.persistence.JsonViews;
import br.com.megahack.api.model.persistence.Stand;
import br.com.megahack.api.repositories.EventRepository;
import br.com.megahack.api.repositories.StandRepository;
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
@RequestMapping("/manager/stand")
public class ManagerStandsController {

    @Autowired
    private StandRepository repository;

    @GetMapping()
    public ResponseEntity<Page<Stand>> getAllEvents(Pageable pageable){
        Page<Stand> event = repository.findAll(pageable);
        return ResponseEntity.ok(event);
    }

    @PostMapping()
    @JsonView(JsonViews.ManagerView.class)
    public ResponseEntity<Stand> createNewStand(@Valid @RequestBody Stand event) {
        Stand newEvent = (Stand) repository.save(event);
            if (newEvent == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.created(URI.create(String.format("manager/stand/%s", event.getUuid()))).body(event);
    }

    @JsonView(JsonViews.ManagerView.class)
    @PutMapping("/{uuid}")
    public ResponseEntity<Event> updateStand(@PathVariable("uuid") String uuid, @Valid @RequestBody Event event) {
//        Event newEvent = repository.updateEvent(event, uuid);
//        if(newEvent == null){
            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(newEvent);
    }
}
