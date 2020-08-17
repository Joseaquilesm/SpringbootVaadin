package com.web.SpringbootVaadin.Vistas;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.web.SpringbootVaadin.Entity.User;
import com.web.SpringbootVaadin.Services.EventService;
import com.web.SpringbootVaadin.Services.UserService;
import com.web.SpringbootVaadin.Entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import org.vaadin.calendar.CalendarComponent;
import org.vaadin.calendar.data.AbstractCalendarDataProvider;

import java.time.ZoneId;
import java.util.*;

@Route("calendario")
@SpringComponent
@UIScope
public class Main extends VerticalLayout {

    public static CalendarComponent<Event> calendario = new CalendarComponent<Event>()
            .withItemDateGenerator(Event::getDate)
            .withItemLabelGenerator(Event::getTitle)
            .withItemThemeGenerator(Event::getColor);

    @Autowired
    public static EventService eventService;

    @Autowired
    public Main(
            @Autowired FormularioEvento formularioEvento,
            @Autowired UserService userService,
            @Autowired EventService eventService,
            @Autowired FormularioEmail formularioEmail,
            @Autowired EditarEvento editarEvento) {
        Main.eventService = eventService;

        if (userService.listarUsuarios().isEmpty()) {
            getUI().get().navigate("");
        } else if (!userService.listarUsuarios().get(0).isLogged()) {
            getUI().get().navigate("");
        } else {
            setAlignItems(Alignment.CENTER);

            HorizontalLayout btns = new HorizontalLayout();
            btns.setSpacing(true);

            Button add = new Button("Agregar Evento");
            add.setIcon(new Icon(VaadinIcon.CALENDAR));
            add.getElement().setAttribute("theme", "primary");

            Button sendMail = new Button("Enviar Email");
            sendMail.setIcon(new Icon(VaadinIcon.CALENDAR_ENVELOPE));

            Button vUser = new Button("Usuario");
            vUser.setIcon(new Icon(VaadinIcon.COG_O));

            Button geren = new Button("Gerentes");
            geren.setIcon(new Icon(VaadinIcon.CLIPBOARD_USER));

            Button ex = new Button("Salir");
            ex.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            ex.getElement().setAttribute("theme", "error");

            cButton(add, formularioEvento);
            cButton(sendMail, formularioEmail);

            btns = new HorizontalLayout(add, sendMail, vUser, geren, ex);

            ex.addClickListener((evento) -> {

                try {
                    User usuario = userService.listarUsuarios().get(0);
                    usuario.setLogged(false);
                    userService.editarUsuario(usuario);

                    getUI().get().navigate("");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            vUser.addClickListener((evento) -> getUI().get().navigate("usuario"));
            geren.addClickListener((evento) -> getUI().get().navigate("gerentes"));

            calendario.setDataProvider(new DataProvider());

            calendario.addEventClickListener(evt -> {
                try {
                    editarEvento.date.setValue(evt.getDetail().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    editarEvento.titl.setValue(evt.getDetail().getTitle());

                    uDialog(editarEvento);

                    eventService.crearEvento(evt.getDetail().getId(), evt.getDetail().getDate(), evt.getDetail().getTitle(), evt.getDetail().getColor());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            H2 h2 = new H2("Calendario con Vaadin");
            setAlignItems(Alignment.CENTER);
            add(h2, btns, calendario);

        }

        Button add = new Button("Add");
        add.setIcon(new Icon(VaadinIcon.PLUS));
        add.getElement().setAttribute("theme", "primary");
    }

    private void uDialog(VerticalLayout form) {
        Dialog ui = new Dialog();
        ui.add(form);

        ui.open();
    }

    private void cButton(Button btn, VerticalLayout form) {
        btn.addClickListener((e) -> {
            uDialog(form);
        });
    }
}

@SpringComponent
@UIScope
class DataProvider extends AbstractCalendarDataProvider<Event> {
    @Override
    public Collection<Event> getItems(Date fromDate, Date toDate) {
        List<Event> eventos = Main.eventService.listarEventos();
        return eventos;
    }
}
