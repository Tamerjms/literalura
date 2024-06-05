package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000) // Ajusta el tamaño según sea necesario
    private String titulo;

    @Column(length = 1000) // Ajusta el tamaño según sea necesario
    private String categoria;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    @Column(length = 1000) // Ajusta el tamaño según sea necesario
    private String lenguaje;

    private Integer descargas;

    public Libro() {}

    public Libro(DatosLibro datosLibro) {
        this.titulo = truncateIfNecessary(datosLibro.titulo(), 1000);
        this.descargas = datosLibro.descargas();
        this.lenguaje = truncateIfNecessary(String.join(", ", datosLibro.lenguajes()), 1000);
        this.categoria = truncateIfNecessary(String.join(", ", datosLibro.categoria()), 1000);

        for (DatosAutor datosAutor : datosLibro.autores()) {
            Autor autor = new Autor(datosAutor.nombre(), datosAutor.anoNacimiento(), datosAutor.anoFallecimiento());
            this.autores.add(autor);
        }
    }

    private String truncateIfNecessary(String value, int maxLength) {
        return (value != null && value.length() > maxLength) ? value.substring(0, maxLength) : value;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", autores=" + autores.stream().map(Autor::getNombre).collect(Collectors.joining(", ")) +
                ", lenguaje='" + lenguaje + '\'' +
                ", descargas=" + descargas +
                '}';
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }
}
