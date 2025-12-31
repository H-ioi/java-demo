package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        System.out.println("\n" +
                "╔════════════════════════════════════════╗\n" +
                "║    Demo Application Started Success!   ║\n" +
                "║                                        ║\n" +
                "║    🚀 Spring Boot 3.1.5              ║\n" +
                "║    🐘 PostgreSQL Ready              ║\n" +
                "║    🔗 http://localhost:8080/api      ║\n" +
                "║    📊 Health: http://localhost:8080/api/users/health ║\n" +
                "║                                        ║\n" +
                "║    Press Ctrl+C to stop the server    ║\n" +
                "╚════════════════════════════════════════╝\n");
    }
}