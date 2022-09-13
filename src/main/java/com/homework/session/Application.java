package com.homework.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
public class Application {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:/application.yml,"
            + "classpath:/application-real.yml";

    public static void main(String[] args){
        new SpringApplicationBuilder(Application.class).properties(APPLICATION_LOCATIONS).run(args);
    }
}
