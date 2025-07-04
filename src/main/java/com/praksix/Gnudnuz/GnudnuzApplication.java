package com.praksix.Gnudnuz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class GnudnuzApplication {

	public static void main(String[] args) {
		SpringApplication.run(GnudnuzApplication.class, args);
	}

}
