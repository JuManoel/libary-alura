package edu.alura.libary.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAuthor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") int anoNasc,
        @JsonAlias("death_year") int anoMorte) {

}
