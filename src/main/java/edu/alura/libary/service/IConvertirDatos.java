package edu.alura.libary.service;

public interface IConvertirDatos {
    <T> T obterDados(String json, Class<T> clase);
}
