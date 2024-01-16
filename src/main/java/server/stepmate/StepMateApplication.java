package server.stepmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StepMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(StepMateApplication.class, args);
	}

}
