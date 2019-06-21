package com.your.drive.yourdrive;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
//		final HikariDataSource ds = new HikariDataSource();
//		ds.setMaximumPoolSize(20);
////		ds.setDriverClassName("org.mariadb.jdbc");
//		ds.setJdbcUrl("jdbc:mysql://localhost:3306/your_drive");
//		ds.addDataSourceProperty("user", "app_user");
//		ds.addDataSourceProperty("password", "root");
//		ds.setAutoCommit(false);
//
//		// Create the Flyway instance and point it to the database
//		Flyway flyway = Flyway.configure().dataSource("jdbc:mysql://localhost:3306/your_drive", "app_user", "root").load();
//
//		// Start the migration
//		flyway.migrate();
		SpringApplication.run(Application.class, args);
	}

}
