package com.iota.tracking.api;

import com.iota.tracking.domain.Event;
import com.iota.tracking.service.IEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping({"/api/iota/tracking/v1/event"})
public class EventResource {
    @Autowired
    IEventService eventService;

    @GetMapping({"/findAllEventsByProductId"})
    public ResponseEntity<List<Event>> findAllEventsByProductId(@RequestParam("id") int id) {
        log.info("findAllEventsByProductId {}", id);
        return ResponseEntity.ok(eventService.findAllByProductfk(id));
    }
}

