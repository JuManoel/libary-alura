package edu.alura.libary.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.alura.libary.models.Book;
import edu.alura.libary.models.Lingua;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b JOIN b.autor a WHERE a.nombre ILIKE :author")
    Optional<List<Book>> cojerLibrosPeloAutor(String author);

    @Query("SELECT b FROM Book b WHERE b.lingua = :l")
    Optional<List<Book>> cojerLibrosPorElIdioma(Lingua l);

    List<Book> findTop10ByOrderByTotDownloadDesc();

}
