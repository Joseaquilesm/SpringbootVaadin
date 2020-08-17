package com.web.SpringbootVaadin.Vistas;


import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.web.SpringbootVaadin.Entity.User;
import com.web.SpringbootVaadin.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;

@Route("")
@SpringComponent
@UIScope

public class Login extends VerticalLayout {

    public Login(
            @Autowired UserService userService) {

        TextField email = new TextField("Email");
        PasswordField passw = new PasswordField("Password");
        TextField name = new TextField("Name");
        Button btnLogin = new Button("Crear");

        btnLogin.setIcon(new Icon(VaadinIcon.SIGN_IN_ALT));
        btnLogin.getElement().setAttribute("theme", "primary");
        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setHeight("100%");
        vLayout.setSizeFull();



        H2 title = new H2("Calendario con Springboot y Vaadin");
        H4 title2 = new H4("Crea una cuenta");
        H4 title3 = new H4("Ingrese con su cuenta");


        if (userService.listarUsuarios().isEmpty()) {
            vLayout = new VerticalLayout(name, email, passw);
            vLayout.setAlignItems(Alignment.CENTER);

        } else {
            vLayout = new VerticalLayout(email, passw);
            vLayout.setAlignItems(Alignment.CENTER);

        }

        btnLogin.addClickListener((evento) -> {
            if (userService.listarUsuarios().isEmpty()) {

                try {
                    userService.crearUsuario(userService.listarUsuarios().size() + 1,
                            name.getValue(), email.getValue(), passw.getValue());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                getUI().get().getPage().reload();


            } else {
                if (userService.validarUsuario(email.getValue(), passw.getValue())) {

                    try {
                        User usuario = userService.listarUsuarios().get(0);
                        usuario.setLogged(true);
                        userService.editarUsuario(usuario);
                        getUI().get().navigate("calendario");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getUI().get().getPage().reload();
                }
            }
        });

        if (userService.listarUsuarios().isEmpty()) {
            setAlignItems(Alignment.CENTER);
            add(title, title2, vLayout, btnLogin);

        } else {
            setAlignItems(Alignment.CENTER);
            add(title, title3, vLayout, btnLogin);
        }

    }
}
