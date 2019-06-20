package com.your.drive.yourdrive;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// Create the Flyway instance and point it to the database
		Flyway flyway = Flyway.configure().dataSource("jdbc:mysql://database:3306/your_drive", "app_user", "root").load();

		// Start the migration
		flyway.migrate();
		SpringApplication.run(Application.class, args);
	}

}
