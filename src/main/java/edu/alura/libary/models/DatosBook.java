package edu.alura.libary.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosBook(
        @JsonAlias("title") String titulo,
        @JsonAlias("languages") List<String> lingua,
        @JsonAlias("authors") List<DatosAuthor> autores,
        @JsonAlias("download_count") int totDownload) {

}
