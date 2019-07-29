package es.sm2baleares.base;

import es.sm2baleares.sf.mockbuilder.config.EnableMockBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point (when its not deployed in an app server).
 */
@SpringBootApplication
@EnableMockBuilder
public class BaseSbootNg2ApiApp {

    public static void main(String[] args) {
        SpringApplication.run(BaseSbootNg2ApiApp.class, args);
    }

}