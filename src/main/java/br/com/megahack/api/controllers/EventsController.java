package br.com.megahack.api.controllers;

import br.com.megahack.api.business.StandardEventBO;
import br.com.megahack.api.business.interfaces.ICustomerBO;
import br.com.megahack.api.model.persistence.JsonViews;
import br.com.megahack.api.model.persistence.Event;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventsController {

    @Autowired
    private StandardEventBO eventBO;

    @Autowired
    private ICustomerBO customerBO;

    @JsonView(JsonViews.SummaryView.class)
    @GetMapping
    public Page<Event> getAllEvents(Pageable pageable) {
        return eventBO.getAllActiveEventsToDisplay(pageable);
    }

//    @JsonView(JsonViews.CustomerView.class)
//    @GetMapping("/{find_by}")
//    public ResponseEntity<Event> getEventBySlug(@PathVariable("find_by") String findBy, @RequestParam(value = "by", defaultValue = "slug") String by) {
//        Event p;
//        if("uuid".equals(by)){
//            p = eventBO.findActiveEventByUuid(findBy);
//        }
//        else {
//            p = eventBO.findActiveEventBySlug(findBy);
//        }
//
//        if (p==null){
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(p);
//    }

    @JsonView(JsonViews.CustomerView.class)
    @GetMapping("/{sku}")
    public ResponseEntity<Event> getEventBySku(@PathVariable("sku") String sku) {
        Event p = eventBO.findActiveEventBuSku(sku);
        if (p==null){ return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(p);
    }

}

