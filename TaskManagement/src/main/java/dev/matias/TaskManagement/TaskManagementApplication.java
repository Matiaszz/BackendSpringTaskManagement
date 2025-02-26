package dev.matias.TaskManagement;

import dev.matias.TaskManagement.utils.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class TaskManagementApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(TaskManagementApplication.class, args);
		Utils.clearScreen();
	}
}
