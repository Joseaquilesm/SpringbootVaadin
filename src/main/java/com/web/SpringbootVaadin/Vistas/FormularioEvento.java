package com.web.SpringbootVaadin.Vistas;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.web.SpringbootVaadin.Services.EventService;
import com.web.SpringbootVaadin.Entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.calendar.CalendarItemTheme;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringComponent
@UIScope
public class FormularioEvento extends VerticalLayout {

    DatePicker date = new DatePicker();
    TextField txt = new TextField("Título");

    public FormularioEvento(
            @Autowired EventService eventoService) {
        FormLayout formLayout = new FormLayout();
        H2 header = new H2("Nuevo evento");

        date.setLabel("Fecha");
        date.setValue(LocalDate.now());

        Button add = new Button("Agregar");
        add.setIcon(new Icon(VaadinIcon.CALENDAR_O));
        add.getElement().setAttribute("theme", "primary");

        Button edit = new Button("Editar");
        edit.setIcon(new Icon(VaadinIcon.EDIT));
        edit.getElement().setAttribute("theme", "warning");

        Button cancel = new Button("Cancelar");
        cancel.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        cancel.getElement().setAttribute("theme", "error");

        HorizontalLayout btns = new HorizontalLayout(add, cancel);
        formLayout.add(txt, date);
        setAlignItems(Alignment.CENTER);
        add(header, formLayout, btns);

        cancel.addClickListener((evento) -> {
            txt.setValue("");
            date.setValue(LocalDate.now());

        });

        add.addClickListener((evento) -> {
            Event e = new Event(
                    eventoService.listarEventos().size() + 1,
                    Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    txt.getValue(),
                    CalendarItemTheme.Green
            );
            eventoService.crearEvento(
                    e.getId(),
                    e.getDate(),
                    e.getTitle(),
                    e.getColor()
            );

            txt.setValue("");
            date.setValue(LocalDate.now());
            Main.calendario.refresh();

        });

    }
}
