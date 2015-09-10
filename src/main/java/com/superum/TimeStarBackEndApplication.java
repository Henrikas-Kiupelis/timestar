package com.superum;

import eu.goodlike.random.Random;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TimeStarBackEndApplication {

	public static void main(String[] args) {
        Random.init(); // to avoid initialization times later on in app; optional
        SpringApplication.run(TimeStarBackEndApplication.class, args);
	}

}
