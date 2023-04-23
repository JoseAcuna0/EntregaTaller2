/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech;

import cl.ucn.disc.pa.bibliotech.services.Sistema;
import edu.princeton.cs.stdlib.StdIn;
import edu.princeton.cs.stdlib.StdOut;

import java.io.IOException;
import java.util.Objects;

/**
 * The Main.
 *
 * @author Programacion Avanzada.
 */
public final class Main {

    /**
     * The main.
     *
     * @param args to use.
     * @throws IOException en caso de un error.
     */
    public static void main(final String[] args) throws IOException {

        // inicializacion del sistema.
        Sistema sistema = new Sistema();

        String opcion = null;
        while (!Objects.equals(opcion, "2")) {

            StdOut.println("""
                    [*] Bienvenido a BiblioTech [*]
                                    
                    [1] Iniciar Sesion
                    [2] Salir
                    """);
            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> iniciarSesion(sistema);
                case "2" -> StdOut.println("¡Hasta Pronto!");
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    /**
     * Inicia la sesion del Socio en el Sistema.
     *
     * @param sistema a utilizar.
     */
    private static void iniciarSesion(final Sistema sistema) {
        StdOut.print("");
        StdOut.println("[*] Iniciar sesion en BiblioTech [*]");
        StdOut.print("Ingrese su numero de socio: ");
        int numeroDeSocio = StdIn.readInt();
        StdIn.readLine();
        StdOut.print("Ingrese su contrasenia: ");
        String contrasenia = StdIn.readLine();

        // intento el inicio de session
        try {
            sistema.iniciarSession(numeroDeSocio, contrasenia);
        } catch (IllegalArgumentException ex) {
            StdOut.println("Ocurrio un error: " + ex.getMessage());
            return;
        }

        // mostrar menu principal
        menuPrincipal(sistema);
    }

    /**
     * Menu
     *
     * @param sistema a utilizar
     */
    private static void menuPrincipal(final Sistema sistema) {
        String opcion = null;
        while (!Objects.equals(opcion, "4")) {
            StdOut.print("");
            StdOut.println("""
                    [*] BiblioTech [*]
                                        
                    [1] Prestamo de un libro
                    [2] Editar información
                    [3] Calificar libro
                                        
                    [4] Cerrar sesion
                    """);

            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> menuPrestamo(sistema);
                case "2" -> editarInformacion(sistema);
                case "3" -> menuCalificar(sistema);
                case "4" -> sistema.cerrarSession();
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }


    /**
     * Menu para el prestamo de algun libro
     *
     * @param sistema a utilizar
     */
    private static void menuPrestamo(Sistema sistema) {
        StdOut.print("");
        StdOut.println("[*] Préstamo de un Libro [*]");
        StdOut.println(sistema.obtegerCatalogoLibros());

        StdOut.print("Ingrese el ISBN del libro a tomar prestado: ");
        String isbn = StdIn.readLine();

        try {
            sistema.realizarPrestamoLibro(isbn);
        } catch (IOException ex) {
            StdOut.println("Ocurrio un error, intente nuevamente: " + ex.getMessage());
        }
    }

    /**
     * Menu para las distintas opciones para editar la informacion del socio
     *
     * @param sistema a utilizar
     */
    private static void editarInformacion(Sistema sistema) {

        String opcion = null;
        while (!Objects.equals(opcion, "3")) {
            StdOut.print("");
            StdOut.println("[*] Editar Perfil [*]");
            StdOut.println(sistema.obtenerDatosSocioLogeado());
            StdOut.println("""               
                    [1] Editar correo Electronico
                    [2] Editar Contraseña
                                        
                    [3] Volver atrás
                    """);
            StdOut.print("Escoja una opción: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> editarCorreo(sistema);
                case "2" -> cambiarContrasenia(sistema);
                case "3" -> StdOut.println("Volviendo al menú anterior...");
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }


    /**
     * Menu para calificar algun libro
     *
     * @param sistema a utilizar
     */
    private static void menuCalificar(Sistema sistema) {
        StdOut.print("");
        StdOut.println("[*] Calificar un libro [*]");
        StdOut.println(sistema.obtegerCatalogoLibros());

        StdOut.print("Ingrese el isbn del libro: ");
        String isbn = StdIn.readLine();

        StdOut.print("Ingrese la una calificiacion del 1 al 7: ");
        int calificacion = StdIn.readInt();
        StdIn.readLine();
        try {
            sistema.calificarLibro(isbn, calificacion);
        } catch (IllegalArgumentException ex) {
            StdOut.println("Ocurrio un error, intente nuevamente: " + ex.getMessage());
        }
    }


    /**
     * Menu para editar la contrasenia
     *
     * @param sistema a utilizar
     */
    private static void cambiarContrasenia(Sistema sistema) {
        StdOut.print("");
        StdOut.println("[*] Cambiar contraseña [*]");
        StdOut.print("Ingrese su contraseña: ");
        String contrasenia = StdIn.readLine();

        try {
            sistema.cambiarContrasenia(contrasenia);
        } catch (IllegalArgumentException ex) {
            StdOut.println("Ocurrio un error, intente nuevamente: " + ex.getMessage());
        }
    }

    /**
     * Menu para editar el correo
     *
     * @param sistema a utilizar
     */
    private static void editarCorreo(Sistema sistema) {
        StdOut.print("");
        StdOut.println("[*] Cambiar Correo electronico [*]");
        StdOut.print("Ingrese su correo electronico: ");
        String correoElectronico = StdIn.readLine();
        StdOut.print("Ingrese su contraseña: ");
        String contrasenia = StdIn.readLine();
        try {
            sistema.cambiarCorreoElectronico(correoElectronico, contrasenia);
        } catch (IllegalArgumentException ex) {
            StdOut.println("Ocurrio un error, intente nuevamente: " + ex.getMessage());

        }
    }
}
