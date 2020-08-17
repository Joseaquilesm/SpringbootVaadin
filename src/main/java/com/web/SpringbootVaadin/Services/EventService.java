package com.web.SpringbootVaadin.Services;

import com.web.SpringbootVaadin.Entity.Event;
import com.web.SpringbootVaadin.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.calendar.CalendarItemTheme;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> listarEventos() {

        return eventRepository.findAll();
    }

    public List<Event> encontrarEventoPorFecha(Date date)
    {
        return eventRepository.findAllByDate(date);
    }

    public List<Event> encontrarEventosEnUnRango(Date starDate, Date endDate) {
        return eventRepository.findByDatesBetween(starDate, endDate);
    }

    public Event encontrarEventoPorID(int id){
        return eventRepository.getOne(id);
    }

    @Transactional
    public Event crearEvento(int id, Date date, String title, CalendarItemTheme color) {
        return eventRepository.save(new Event(id, date, title, color));
    }

    @Transactional
    public boolean borrarEvento(Event event) {
        eventRepository.delete(event);
        return true;
    }

    public void editarEvento(int EventID) throws Exception {

        Event event = encontrarEventoPorID(EventID);
        eventRepository.save(event);

    }
}
