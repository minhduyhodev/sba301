package fu.se.chapter13orchid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"fu.se.chapter13orchid.controllers", "fu.se.chapter13orchid.services"})
@EnableJpaRepositories(basePackages = "fu.se.chapter13orchid.repositories")
@EntityScan(basePackages = "fu.se.chapter13orchid.pojos")
public class Chapter13orchidApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter13orchidApplication.class, args);
    }
}
