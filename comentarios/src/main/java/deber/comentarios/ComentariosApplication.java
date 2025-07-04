package deber.comentarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "deber.comentarios.repositories")
public class ComentariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComentariosApplication.class, args);
	}

}
