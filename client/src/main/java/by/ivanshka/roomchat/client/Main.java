package by.ivanshka.roomchat.client;

import by.ivanshka.roomchat.client.ui.MainController;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(Main.class)
                .web(WebApplicationType.NONE)
                .run(args);

        MainController mainController = (MainController) context.getBean("mainController");
        mainController.start();
    }
}