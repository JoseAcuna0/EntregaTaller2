/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.services;

import cl.ucn.disc.pa.bibliotech.model.Libro;
import cl.ucn.disc.pa.bibliotech.model.Socio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.princeton.cs.stdlib.StdIn;
import edu.princeton.cs.stdlib.StdOut;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * The Sistema.
 *
 * @author Programacion Avanzada.
 */
public final class Sistema {

    /**
     * Procesador de JSON.
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The list of Socios.
     */
    private Socio[] socios;

    /**
     * The list of Libros.
     */
    private Libro[] libros;

    /**
     * Socio en el sistema.
     */
    private Socio socio;

    /**
     * The Sistema.
     */
    public Sistema() throws IOException {

        // no hay socio logeado.
        this.socios = new Socio[0];
        this.libros = new Libro[0];
        this.socio = null;

        // carga de los socios y libros.
        try {
            this.cargarInformacion();
        } catch (FileNotFoundException ex) {
            // no se encontraron datos, se agregar los por defecto.

            // creo un socio
            this.socios = Utils.append(this.socios, new Socio("John", "Doe", "john.doe@ucn.cl", 1, "john123"));

            // creo un libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("1491910771", "Head First Java: A Brain-Friendly Guide", "Kathy Sierra", "Programming Languages", 0));

            // creo otro libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("2481910774", "Effective Java", "Joshua Bloch", "Programming Languages", 0));

        } finally {
            // guardo la informacion.
            this.guardarInformacion();
        }

    }

    /**
     * Activa (inicia sesion) de un socio en el sistema.
     *
     * @param numeroDeSocio a utilizar.
     * @param contrasenia   a validar.
     */
    public void iniciarSession(final int numeroDeSocio, final String contrasenia) {

        // El numero de socio siempre es positivo.
        if (numeroDeSocio <= 0) {
            throw new IllegalArgumentException("El numero de socio no es valido!");
        }

        //Busca el nombre del socio atravez del numero de socio
        for (int i = 0; i < socios.length; i++) {
            if (this.socios[i].getNumeroDeSocio() == numeroDeSocio) {
                this.socios[i].getNombre();

            } else {
                throw new IllegalArgumentException("Numero de socio no encontrado ");
            }
        }

        //se verifica la clave
        for (int i = 0; i < socios.length; i++) {
            if (this.socios[i].getNumeroDeSocio() == numeroDeSocio) {
                if (Objects.equals(this.socios[i].getContrasenia(), contrasenia)) {
                    StdOut.println("Bienvenido " + this.socios[i].getNombre());
                } else {
                    throw new IllegalArgumentException("Contraseña incorrecta ");
                }
            }
        }
        //se asigna el atributo de socio
        for (int i = 0; i < socios.length; i++) {
            if (this.socios[i].getNumeroDeSocio() == numeroDeSocio) {
                this.socio = this.socios[i];
            }
        }
    }


    /**
     * Cierra la session del Socio.
     */
    public void cerrarSession() {
        this.socio = null;
    }

    /**
     * Metodo que mueve un libro de los disponibles y lo ingresa a un Socio.
     *
     * @param isbn del libro a prestar.
     */
    public void realizarPrestamoLibro(final String isbn) throws IOException {
        // el socio debe estar activo.
        if (this.socio == null) {
            throw new IllegalArgumentException("Socio no se ha logeado!");
        }

        // busco el libro.
        Libro libro = this.buscarLibro(isbn);

        // si no lo encontre, lo informo.
        if (libro == null) {
            throw new IllegalArgumentException("Libro con isbn " + isbn + " no existe o no se encuentra disponible.");
        }

        // agrego el libro al socio.
        this.socio.agregarLibro(libro);

        //eliminar el libro de la lista de los disponibles
        for (int i = 0; i < libros.length; i++) {
            if (this.libros[i].getIsbn() == isbn) {
                this.libros[i] = null;
            }
        }

        // se actualiza la informacion de los archivos
        this.guardarInformacion();

    }

    /**
     * Agrega la calificacion al libro y si hay mas usuarios saca el promedio
     *
     * @param isbn         a utilizar
     * @param calificacion a utilizar
     */
    public void calificarLibro(final String isbn, final int calificacion) {
        int totalSocios = 0;
        //Se crea un contador de total de socios
        for (int j = 0; j < socios.length; j++) {
            totalSocios = totalSocios + 1;
        }
        //Se recorre los libros
        for (int i = 0; i < libros.length; i++) {
            //Busqueda del libro
            if (Objects.equals(this.libros[i].getIsbn(), isbn)) {
                StdOut.println("Agregaste una calificacion de nota " + calificacion + " al libro " + this.libros[i].getTitulo());
                //Si no se ha calificado se agrega la calificacion
                if (this.libros[i].getCalificacion() == 0) {
                    this.libros[i].setCalificacion(calificacion);
                    //Si ya esta calificado se hace un promedio entre la suma de las calificaciones y se divide en el total de socios
                } else {
                    int ola = this.libros[i].getCalificacion();
                    int promedio = (ola + calificacion) / totalSocios;
                    this.libros[i].setCalificacion(promedio);

                }
            }
        }
    }

    /**
     * Obtiene un String que representa el listado completo de libros disponibles.
     *
     * @return the String con la informacion de los libros disponibles.
     */
    public String obtegerCatalogoLibros() {

        StringBuilder sb = new StringBuilder();
        for (Libro libro : this.libros) {
            sb.append("Titulo    : ").append(libro.getTitulo()).append("\n");
            sb.append("Autor     : ").append(libro.getAutor()).append("\n");
            sb.append("ISBN      : ").append(libro.getIsbn()).append("\n");
            sb.append("Categoria : ").append(libro.getCategoria()).append("\n");
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Metodo que busca un libro en los libros disponibles.
     *
     * @param isbn a buscar.
     * @return el libro o null si no fue encontrado.l
     */
    private Libro buscarLibro(final String isbn) {
        // recorro el arreglo de libros.
        for (Libro libro : this.libros) {
            // si lo encontre, retorno el libro.
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        // no lo encontre, retorno null.
        return null;
    }

    /**
     * Lee los archivos libros.json y socios.json.
     *
     * @throws FileNotFoundException si alguno de los archivos no se encuentra.
     */
    private void cargarInformacion() throws FileNotFoundException {

        // trato de leer los socios y los libros desde el archivo.
        this.socios = GSON.fromJson(new FileReader("socios.json"), Socio[].class);
        this.libros = GSON.fromJson(new FileReader("libros.json"), Libro[].class);
    }

    /**
     * Guarda los arreglos libros y socios en los archivos libros.json y socios.json.
     *
     * @throws IOException en caso de algun error.
     */
    private void guardarInformacion() throws IOException {

        // guardo los socios.
        try (FileWriter writer = new FileWriter("socios.json")) {
            GSON.toJson(this.socios, writer);
        }

        // guardo los libros.
        try (FileWriter writer = new FileWriter("libros.json")) {
            GSON.toJson(this.libros, writer);
        }

    }

    /**
     * Obtiene los datos del socio actual
     *
     * @return Datos del socio
     */
    public String obtenerDatosSocioLogeado() {
        if (this.socio == null) {
            throw new IllegalArgumentException("No hay un Socio logeado");
        }

        return "Nombre: " + this.socio.getNombreCompleto() + "\n"
                + "Correo Electronico: " + this.socio.getCorreoElectronico();
    }


    /**
     * Cambia la contrasenia actual a una nueva
     *
     * @param contrasenia a utilizar
     */
    public void cambiarContrasenia(final String contrasenia) {

        //Valida la contrasenia para luego poner la nueva
        if (Objects.equals(this.socio.getContrasenia(), contrasenia)) {
            StdOut.println("Ingrese la nueva contraseña que desea ulitizar: ");
            String contraseniaNueva = StdIn.readLine();
            this.socio.setContrasenia(contraseniaNueva);

            //Si la contrasenia no es igual tira el error
        } else {
            throw new IllegalArgumentException("Contraseña incorrecta ");
        }
    }

    /**
     * Cambia el correo actual por uno nuevo
     *
     * @param correoElectronico a utilizar
     * @param contrasenia       a utilizar
     */
    public void cambiarCorreoElectronico(final String correoElectronico, final String contrasenia) {
        if (Objects.equals(this.socio.getCorreoElectronico(), correoElectronico)) {
            if (Objects.equals(this.socio.getContrasenia(), contrasenia)) {
                StdOut.println("Ingrese el nuevo correo que desea utilizar: ");
                String correoElectronicoNuevo = StdIn.readLine();
                this.socio.setCorreoElectronico(correoElectronicoNuevo);
                StdOut.println(this.socio.getCorreoElectronico());

            } else {
                throw new IllegalArgumentException("Contraseña incorrecta ");
            }
        }
    }
}