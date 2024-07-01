package edu.alura.libary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.alura.libary.principal.Principal;
import edu.alura.libary.repository.AuthorRepository;
import edu.alura.libary.repository.BookRepository;

@SpringBootApplication
public class LibaryApplication implements CommandLineRunner {

	@Autowired
	BookRepository repository;
	@Autowired
	AuthorRepository repository2;
	public static void main(String[] args) {
		SpringApplication.run(LibaryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal menu = new Principal(repository,repository2);
		menu.menu();
	}

}
