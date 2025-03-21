package source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/*
* O que está faltando: criar uma conta no Heroku, fazer o Deploy e tudo o mais...
* */

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.banco_online_sil.repository", "com.example.banco_online_sil.controller"})
@EntityScan("source.model")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}