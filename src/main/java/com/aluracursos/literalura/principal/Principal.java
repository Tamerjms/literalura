package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.AutorService;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "http://gutendex.com/books/";
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final LibroRepository repositorio;
    private final AutorService autorService;

    @Autowired
    public Principal(LibroRepository repositorio, AutorService autorService) {
        this.repositorio = repositorio;
        this.autorService = autorService;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Buscar Autor por nombre
                    3 - Listar libros registrados
                    4 - Listar autores registrados
                    5 - Listar autores vivos en un determinado año
                    6 - Listar libros por idioma
                    7 - Top 10 libros mas descargados
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    buscarAutorPorNombre();
                    break;
                case 3:
                    listarLibrosRegistrados();
                    break;
                case 4:
                    listarAutoresRegistrados();
                    break;
                case 5:
                    listarAutoresVivosEnDeterminadoAno();
                    break;
                case 6:
                    listarLibrosPorIdioma();
                    break;
                case 7:
                    top10LibrosMasDescargados();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }

    }

    private void buscarLibroPorTitulo() {
        System.out.println("Por favor digita el nombre del libro que deseas buscar: ");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.toLowerCase().replace(" ", "%20"));
        if (json == null || json.isEmpty()) {
            System.out.println("La respuesta de la API está vacía");
            return;
        }

        try {
            ResultadoBusqueda resultado = conversor.obtenerDatos(json, ResultadoBusqueda.class);
            List<DatosLibro> datosLibros = resultado.getResultados();
            for (DatosLibro datosLibro : datosLibros) {
                for (DatosAutor datosAutor : datosLibro.autores()) {
                    Autor autor = new Autor(datosAutor.nombre(), datosAutor.anoNacimiento(), datosAutor.anoFallecimiento());
                    autor = autorService.agregarAutor(autor);
                }

                if (!repositorio.existsByTitulo(datosLibro.titulo())) {
                    Libro libro = new Libro(datosLibro);
                    repositorio.save(libro);
                } else {
                    System.out.println("El libro \"" + datosLibro.titulo() + "\" ya existe en la base de datos.");
                }
            }
            System.out.println("Proceso de agregación de libros completado.");
        } catch (RuntimeException e) {
            System.out.println("Error al deserializar el JSON: " + e.getMessage());
        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("Por favor, digita el nombre del autor que deseas buscar: ");
        var nombreAutor = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreAutor.toLowerCase().replace(" ", "%20"));
        if (json == null || json.isEmpty()) {
            System.out.println("La respuesta de la API está vacía");
            return;
        }

        try {
            ResultadoBusqueda resultado = conversor.obtenerDatos(json, ResultadoBusqueda.class);
            List<DatosLibro> datosLibros = resultado.getResultados();
            List<DatosAutor> autores = datosLibros.stream()
                    .flatMap(libro -> libro.autores().stream())
                    .filter(autor -> autor.nombre().toLowerCase().contains(nombreAutor.toLowerCase()))
                    .distinct()
                    .collect(Collectors.toList());
            autores.forEach(System.out::println);
        } catch (RuntimeException e) {
            System.out.println("Error al deserializar el JSON: " + e.getMessage());
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = repositorio.findAll();
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Libro> libros = repositorio.findAll();
        List<Autor> autores = libros.stream()
                .flatMap(libro -> libro.getAutores().stream())
                .distinct()
                .collect(Collectors.toList());
        autores.forEach(autor -> System.out.println(autor.getNombre()));
    }

    private void listarAutoresVivosEnDeterminadoAno() {
        System.out.println("Por favor digita el año: ");
        int ano = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = autorService.obtenerAutoresVivosEnAno(ano);
        autores.forEach(autor -> {
            Integer anoNacimiento = autor.getAnoNacimiento();
            Integer anoFallecimiento = autor.getAnoFallecimiento();

            if (anoNacimiento != null && (anoFallecimiento == null || anoFallecimiento >= ano) && anoNacimiento <= ano) {
                DatosAutor datosAutor = new DatosAutor(autor.getNombre(), anoNacimiento, anoFallecimiento);
                System.out.println(datosAutor);
            }
        });
    }


    private void listarLibrosPorIdioma() {
        System.out.println("Por favor digita el idioma (por ejemplo: 'en' para inglés): ");
        String idioma = teclado.nextLine();

        List<Libro> libros = repositorio.findAll();
        List<Libro> librosPorIdioma = libros.stream()
                .filter(libro -> libro.getLenguaje().contains(idioma))
                .collect(Collectors.toList());
        librosPorIdioma.forEach(System.out::println);
    }

    private void top10LibrosMasDescargados() {
        List<Libro> libros = repositorio.findAll();
        List<Libro> top10Libros = libros.stream()
                .sorted((libro1, libro2) -> libro2.getDescargas().compareTo(libro1.getDescargas()))
                .limit(10)
                .collect(Collectors.toList());
        top10Libros.forEach(System.out::println);
    }
}
