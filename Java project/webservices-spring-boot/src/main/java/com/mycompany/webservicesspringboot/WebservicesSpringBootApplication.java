package com.mycompany.webservicesspringboot;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebservicesSpringBootApplication {
	public static void main(String[] args) {
                Logger.getLogger("org").setLevel(Level.OFF);
                Logger.getLogger("akka").setLevel(Level.OFF);
		SpringApplication.run(WebservicesSpringBootApplication.class, args);
	}

}