package edu.alura.libary.models;

public enum Lingua {
    INGLISH("en"),
    PORTUGUESE("pt"),
    SPANISH("es"),
    FRENCH("fr"),
    ITALIAN("it"),
    JAPANESE("ja"),
    CHINESE("zh"),
    KOREAN("ko");

    private String language;

    private Lingua(String language) {
        this.language = language;
    }

    public static Lingua fromString(String language) {
        for (Lingua lingua : Lingua.values()) {
            if (lingua.language.equalsIgnoreCase(language))
                return lingua;
        }
        throw new IllegalArgumentException(language + " is not supported");
    }

    public String getLanguage() {
        return language;
    }
}
