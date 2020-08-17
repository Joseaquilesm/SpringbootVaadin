package com.web.SpringbootVaadin.Repository;

import com.web.SpringbootVaadin.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByDate(Date date);

    @Query("select evento from Event evento where evento.date between ?1 and ?2")
    List<Event> findByDatesBetween(Date startDate, Date endDate);

}
