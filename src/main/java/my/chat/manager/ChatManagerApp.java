package my.chat.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ChatManagerApp {

    public static void main(String[] args) {
        SpringApplication.run(ChatManagerApp.class, args);
    }
}
