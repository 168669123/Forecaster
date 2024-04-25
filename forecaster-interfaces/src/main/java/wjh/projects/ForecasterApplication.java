package wjh.projects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ForecasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForecasterApplication.class, args);
    }
}
