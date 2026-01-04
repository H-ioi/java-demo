package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(DemoApplication.class, args);
        var env = context.getBean(Environment.class);
        printBanner(env);
    }

    public static void printBanner(Environment env) {
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "/");
        String baseUrl = "http://localhost:" + port + contextPath;
        
        System.out.println("\n" +
                "╔════════════════════════════════════════╗\n" +
                "║    Demo Application Started Success!   ║\n" +
                "║                                        ║\n" +
                "║    🚀 Spring Boot 3.2.0              ║\n" +
                "║    🐘 PostgreSQL Ready              ║\n" +
                "║    🔗 " + baseUrl + "      ║\n" +
                "║    📊 Health: " + baseUrl + "/users/health ║\n" +
                "║                                        ║\n" +
                "║    Press Ctrl+C to stop the server    ║\n" +
                "╚════════════════════════════════════════╝\n");
    }
}