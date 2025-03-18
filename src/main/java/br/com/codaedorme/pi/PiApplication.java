package br.com.codaedorme.pi;

import br.com.codaedorme.pi.infra.view.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PiApplication implements CommandLineRunner {

	@Autowired
	private Menu menu;

	public static void main(String[] args) {
		SpringApplication.run(PiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		menu.inicio();
	}
}
