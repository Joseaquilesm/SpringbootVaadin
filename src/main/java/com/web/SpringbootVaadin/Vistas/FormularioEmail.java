package com.web.SpringbootVaadin.Vistas;

import com.sendgrid.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.io.IOException;

@SpringComponent
@UIScope
public class FormularioEmail extends VerticalLayout {

    TextField destino = new TextField("Para:");
    TextField asunto = new TextField("Asunto:");
    TextArea cuerpo = new TextArea("Cuerpo:");

    public FormularioEmail() {

        FormLayout fm = new FormLayout();

        H2 header = new H2("Enviar Email");

        Button send = new Button("Enviar");
        send.setIcon(new Icon(VaadinIcon.ARROW_FORWARD));
        send.getElement().setAttribute("theme", "primary");

        Button canc = new Button("Cancelar");
        canc.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        canc.getElement().setAttribute("theme", "error");

        HorizontalLayout btns = new HorizontalLayout(send, canc);

        fm.add(destino, asunto, cuerpo);
        setAlignItems(Alignment.CENTER);

        add(header, fm, btns);

        send.addClickListener((evento) -> {

            Email sender = new Email("joseaquilesm@gmail.com");
            Email receiver = new Email(destino.getValue());

            String subjE = destino.getValue();
            Content bodyE = new Content("text/plain", cuerpo.getValue());

            Mail email = new Mail(sender, subjE, receiver, bodyE);

            //Aquí va la llave que se creó con la cuenta de sendgrid
            String apiKey = "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";
            SendGrid sendg = new SendGrid(apiKey);
            Request request = new Request();

            //enviando el email
            try {
                request.method = Method.POST;
                request.endpoint = "mail/send";
                request.body = email.build();
                Response response = sendg.api(request);
                System.out.println(response.statusCode);
                System.out.println(response.body);
                System.out.println(response.headers);

                destino.setValue("");
                asunto.setValue("");
                cuerpo.setValue("");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        canc.addClickListener((evento) -> {
            destino.setValue("");
            asunto.setValue("");
            cuerpo.setValue("");
        });
    }
}
