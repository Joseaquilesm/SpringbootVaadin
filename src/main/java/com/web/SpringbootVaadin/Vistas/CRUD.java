package com.web.SpringbootVaadin.Vistas;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.web.SpringbootVaadin.Entity.User;
import com.web.SpringbootVaadin.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("gerentes")
@SpringComponent
@UIScope
public class CRUD extends VerticalLayout {

    DataProvider<User, Void> dataProvider;
    boolean flagEditar = false;
    int Id;


    public CRUD(

            @Autowired UserService usuarioService) {

        TextField name = new TextField("Name:");
        TextField mail = new TextField("Email:");
        PasswordField passw = new PasswordField("Password:");

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return usuarioService.listarUsuariosPaginados(offset, limit).stream();
                },
                query -> Math.toIntExact(usuarioService.contarUsuario() - 1)
        );

        Binder<User> binder = new Binder<>();
        Grid<User> tbl = new Grid<>();

        Button add = new Button("Save");
        add.setIcon(new Icon(VaadinIcon.CHECK));
        add.getElement().setAttribute("theme", "primary");

        Button canc = new Button("Cancel");
        canc.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        canc.getElement().setAttribute("theme", "error");

        Gerente gerente = new Gerente();

        if (usuarioService.listarUsuarios().isEmpty())
            getUI().get().navigate("");

        else if (!usuarioService.listarUsuarios().get(0).isLogged())
            getUI().get().navigate("");

        else {
            add.addClickListener((evento) -> {
                try {
                    if (flagEditar) {
                        usuarioService.crearUsuario(Id, name.getValue(),
                                mail.getValue(), passw.getValue());
                    } else {
                        usuarioService.crearUsuario(usuarioService.contarUsuario(),
                                name.getValue(), mail.getValue(), passw.getValue());
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

                name.setValue("");
                mail.setValue("");
                passw.setValue("");

                dataProvider.refreshAll();
            });

            canc.addClickListener((evento) -> {
                name.setValue("");
                mail.setValue("");
                passw.setValue("");
            });


            H2 h2 = new H2("Calendario con Vaadin");
            H4 h4 = new H4("Gerentes");

            HorizontalLayout btns = new HorizontalLayout();

            Button calend = new Button("Atras");
            calend.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_LEFT_O));

            Button ex = new Button("Salir");
            ex.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            ex.getElement().setAttribute("theme", "error");

            btns.add(calend, ex);

            ex.addClickListener((evento) -> {

                try {
                    User usuarioaux = usuarioService.listarUsuarios().get(0);
                    usuarioaux.setLogged(false);
                    usuarioService.editarUsuario(usuarioaux);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                getUI().get().navigate("");
            });

            calend.addClickListener((evento) -> getUI().get().navigate("calendario"));

            HorizontalLayout btns2 = new HorizontalLayout(add, canc);
            btns2.setSpacing(true);

            name.setTitle("Name: ");
            mail.setTitle("Email: ");
            passw.setTitle("Password: ");

            tbl.setDataProvider(dataProvider);
            tbl.addColumn(User::getName).setHeader("Name");
            tbl.addColumn(User::getMail).setHeader("Email");

            tbl.addSelectionListener(event -> {

                if (event.getFirstSelectedItem().isPresent()) {
                    popDialog(gerente);
                    gerente.eliminar.addClickListener((evento) -> {
                        User usuario = event.getFirstSelectedItem().get();
                        usuarioService.eliminarUsuario((int) usuario.getId());
                        binder.readBean(usuario);

                        dataProvider.refreshAll();
                    });

                    gerente.modificar.addClickListener((evento) -> {
                        User usuario = event.getFirstSelectedItem().get();

                        name.setValue(usuario.getName());
                        mail.setValue(usuario.getMail());
                        passw.setValue(usuario.getPassword());
                        flagEditar = true;
                        Id = (int) usuario.getId();

                        try {
                            binder.writeBean(usuario);
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }

                    });
                }
            });

            setAlignItems(Alignment.CENTER);
            FormLayout form = new FormLayout(name, mail, passw);

            add(h2, h4, btns, form, btns2, tbl);

            name.setValue("");
            mail.setValue("");
            passw.setValue("");
        }
    }

    private void popDialog(VerticalLayout form) {
        Dialog pD = new Dialog();
        pD.add(form);

        pD.open();
    }
}
