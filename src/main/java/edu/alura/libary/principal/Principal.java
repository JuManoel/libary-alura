package edu.alura.libary.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.dao.DataIntegrityViolationException;

import edu.alura.libary.models.*;
import edu.alura.libary.repository.*;
import edu.alura.libary.service.ApiResponse;
import edu.alura.libary.service.ConsumoAPI;
import edu.alura.libary.service.ConvierteDatos;

public class Principal {
    private ConsumoAPI api = new ConsumoAPI();
    private ConvierteDatos convierte = new ConvierteDatos();
    private String URL = "https://gutendex.com/books/?search=";
    private Scanner scn = new Scanner(System.in);
    private String json;


    //due to the nature of one author has many books
    //but the API send a book with a author
    //I saw a nescessity to make 2 repository, one just to save and make requests for books
    //another to save and make requests for authors
    //the repository2 also can save Author and book at once (do to One to Many stuff)

    private BookRepository repository; // repository ONLY for books
    private AuthorRepository repository2; // repository ONLY for Author



    private List<Book> books;
    private List<Author> authors;

    public Principal(BookRepository repository, AuthorRepository repository2) {
        this.repository = repository;
        this.repository2 = repository2;
    }

    public void menu() {
        String menu = """
                1 - Agregar Libro     5 - Listar Autores vivos en un periodo
                2 - Listar Libros     6 - Listar Libros por idioma
                3 - Listar Autores    7 - Mostrar Top10 Libros
                4 - Buscar Author     8 - Salir
                """;
        int resp;
        do {
            System.out.println("================================================================");
            System.out.println(menu);
            resp = scn.nextInt();
            scn.nextLine();
            switch (resp) {
                case 1:
                    System.out.println("Digite o titulo do livro");
                    String titulo = scn.nextLine();
                    buscarLibro(titulo);
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    System.out.println("Ingrese el nombre del Autor");
                    String nombre = scn.nextLine();
                    buscarAutorNombre(nombre);
                    break;
                case 5:
                    System.out.println("Ingrese el año que buscas");
                    int ano = scn.nextInt();
                    scn.nextLine();
                    listarAutoresVivos(ano);
                    break;
                case 6:
                    System.out.println("Selecione el idioma");
                    for (int i = 0; i < Lingua.values().length; i++) {
                        System.out.println((i + 1) + " - " + Lingua.values()[i].getLanguage());
                    }
                    int idioma = scn.nextInt();
                    scn.nextLine();
                    listarLibrosPorIdioma(idioma);
                    break;
                case 7:
                    top10Libros();
                    break;
                case 8:
                    System.out.println("Hasta Luego :3");
                    break;
                default:
                    System.out.println("Digite uma opção valida");
                    break;
            }
        } while (resp != 8);
    }

   private void top10Libros() {
        books = repository.findTop10ByOrderByTotDownloadDesc();
        for (Book book : books) {
            System.out.println("================================================================");
            System.out.println(book);
        }
        System.out.println("================================================================");
    }

 private void buscarAutorNombre(String nombre) {
        Optional<Author> author = repository2.findByNombreContainsIgnoreCase(nombre);
        if(!author.isPresent()){
            System.out.println("No existe el author: "+nombre);
            return;
        }
        System.out.println(author.get());
        
    }

    private void listarLibrosPorIdioma(int idioma) {
        Lingua l = Lingua.values()[idioma - 1];
        Optional<List<Book>> books = repository.cojerLibrosPorElIdioma(l);
        if (books.isPresent()) {
            for (Book book : books.get()) {
                System.out.println("================================================================");
                System.out.println(book);
            }
            System.out.println("================================================================");
        }
    }

    private void listarAutoresVivos(int ano) {
        Optional<List<Author>> authors = repository2.cojerAutoresVivos(ano);
        if (authors.isPresent()) {
            for (Author author : authors.get()) {
                System.out.println("================================================================");
                System.out.println(author);
            }
            System.out.println("================================================================");
        }
    }

    private void listarAutores() {
        authors = repository2.findAll();
        for (Author author : authors) {
            System.out.println("================================================================");
            System.out.println(author);
        }
        System.out.println("================================================================");
    }

    private void listarLibros() {
        books = repository.findAll();
        for (Book book : books) {
            System.out.println("================================================================");
            System.out.println(book);
        }
        System.out.println("================================================================");
    }

    public void buscarLibro(String titulo) {
        /*
         * buscarLibro(String)
         * this method use my API to get the book with the name is titulo
         * First I make a modification on titulo becouse it must be a url string
         * Then I collect the data using String json and converte to ApiResponse
         * I take the first book and show to the user
         * After that I make sure that the author or the book isnt already in the database
         * if the author is in database but the book is not, no problem, only save the book
         * and make sure that the book has the author refence
         * if the author and book already, send a mensaje to the user, explaining that the book already exists
         * otherwise, sabe normaly, the book and the author
         */
        titulo = titulo.toLowerCase();
        titulo = titulo.replace(' ', '+');

        json = api.obtenerDatos(URL + titulo);
        ApiResponse datosLibro = this.convierte.obterDados(json, ApiResponse.class);

        DatosBook book = datosLibro.bookList().get(0);
        Book libro = new Book(book);
        System.out.println(libro);

        Author author = libro.getAutor();
        List<Book> books = author.getBooks();
        Optional<Author> authorOptional = repository2.findByNombreIgnoreCase(author.getNombre());

        if (books == null)
            books = new ArrayList<>();
        
        if (authorOptional.isPresent()) {
            try {
                libro.setAutores(authorOptional.get());
                repository.save(libro);
                System.out.println("Libro guardado con sucesso!");
                return;
            } catch (DataIntegrityViolationException e) {
                System.out.println("El libro ya existe!");
                System.out.println(libro);
                return;
            }

        }

        books.add(libro);
        author.setBooks(books);
        repository2.save(author);
        System.out.println("Libro guardado con sucesso!");
    }

}
