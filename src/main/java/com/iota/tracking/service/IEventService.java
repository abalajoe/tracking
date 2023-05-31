package com.iota.tracking.service;

import com.iota.tracking.domain.Event;

import java.util.List;

public interface IEventService {
    List<Event> findAllByProductfk(int id);
}
