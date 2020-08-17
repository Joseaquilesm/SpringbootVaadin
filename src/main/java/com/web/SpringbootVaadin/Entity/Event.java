package com.web.SpringbootVaadin.Entity;

import org.vaadin.calendar.CalendarItemTheme;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Event implements Serializable {

    @Id
    private int id;
    private Date date;
    private String title;
    private CalendarItemTheme color;

    public Event() {
    }

    public Event(int id, Date date, String title, CalendarItemTheme color) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CalendarItemTheme getColor() {
        return color;
    }

    public void setColor(CalendarItemTheme color) {
        this.color = color;
    }
}