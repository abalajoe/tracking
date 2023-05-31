package com.iota.tracking.serviceimpl;

import com.iota.tracking.domain.Event;
import com.iota.tracking.repository.EventRepository;
import com.iota.tracking.service.IEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class EventServiceImpl implements IEventService {

    @Autowired
    EventRepository eventRepository;

    @Override
    public List<Event> findAllByProductfk(int id) {
        return eventRepository.findAllByProductfkOrderByIdDesc(id);
    }
}
