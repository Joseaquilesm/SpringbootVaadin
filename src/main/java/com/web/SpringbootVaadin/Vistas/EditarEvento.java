package com.web.SpringbootVaadin.Vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.web.SpringbootVaadin.Entity.Event;
import com.web.SpringbootVaadin.Services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.calendar.CalendarItemTheme;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringComponent
@UIScope
public class EditarEvento extends VerticalLayout {

    DatePicker date = new DatePicker();
    TextField titl = new TextField("Titulo");

    public EditarEvento(@Autowired EventService eventService) {
        FormLayout formLayout = new FormLayout();

        H3 header = new H3("Change Event");

        date.setPlaceholder("Select a date");
        date.setValue(LocalDate.now());

        Button edit = new Button("Edit");
        edit.setIcon(new Icon(VaadinIcon.PENCIL));
        edit.getElement().setAttribute("theme", "success");

        Button canc = new Button("Cancel");
        canc.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        canc.getElement().setAttribute("theme", "error");

        HorizontalLayout botones = new HorizontalLayout(edit, canc);
        botones.setSpacing(true);

        formLayout.add(titl, date);

        setAlignItems(Alignment.CENTER);

        add(header, formLayout, botones);

        edit.addClickListener((evento) -> {

            Event e = new Event(

                    eventService.listarEventos().size(),
                    Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    titl.getValue(),
                    CalendarItemTheme.Blue
            );
            eventService.crearEvento(
                    e.getId(),
                    e.getDate(),
                    e.getTitle(),
                    e.getColor()
            );

            Main.calendario.refresh();
        });

    }
}
