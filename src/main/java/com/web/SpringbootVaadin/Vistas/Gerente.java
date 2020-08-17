package com.web.SpringbootVaadin.Vistas;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class Gerente extends VerticalLayout {
    Button modificar = new Button("Modify");
    Button eliminar = new Button("Delete");

    public Gerente() {
        modificar.getElement().setAttribute("theme", "success");
        modificar.setIcon(new Icon(VaadinIcon.PENCIL));
        eliminar.getElement().setAttribute("theme", "error");
        eliminar.setIcon(new Icon(VaadinIcon.TRASH));
        HorizontalLayout buttons = new HorizontalLayout(modificar, eliminar);
        buttons.setSpacing(true);
        setAlignItems(Alignment.CENTER);
        add(buttons);

    }
}
