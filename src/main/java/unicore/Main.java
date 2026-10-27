package unicore;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Sistema sistema = new Sistema();

        // ==============================
        // CARGAR DESDE LA BASE DE DATOS
        // ==============================

        sistema.inicializarDesdeBaseDatos();

        if (sistema.getUsuarios().isEmpty()) {

            Profesor profesor = new Profesor(
                1, "Carlos Rodriguez", "carlos@unicore.com", "1234",
                "Programación", "Ingenieria de sistemas"
            );

            Estudiante estudiante = new Estudiante(
                2, "Dann", "dann@unicore.com", "1234",
                "2026001", "Ingenieria de sistemas", 1
            );

            Administrador administrador = new Administrador(
                3, "Laura Gomez", "laura@unicore.com", "123",
                "Administrador académico"
            );

            sistema.registrarUsuario(profesor);
            sistema.registrarUsuario(estudiante);
            sistema.registrarUsuario(administrador);

            sistema.registrarMateria(new Materia(1, "Programación Orientada a Objetos", 4, profesor));
            sistema.registrarMateria(new Materia(2, "Bases de Datos", 3, profesor));
        }

        // ==============================
        // AUTENTICACIÓN
        // ==============================

        Autenticacion autenticacion = new Autenticacion();

        System.out.println("=================================");
        System.out.println("          UNICORE");
        System.out.println("     PLATAFORMA ACADÉMICA");
        System.out.println("=================================");
        System.out.println();

        System.out.print("Correo: ");
        String correo = scanner.nextLine();

        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        Usuarios usuarioEncontrado =
                autenticacion.iniciarSesion(
                    correo,
                    contrasena,
                    sistema.getUsuarios()
                );

        // ==============================
        // RESULTADO
        // ==============================

        if (usuarioEncontrado != null) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("     INICIO DE SESIÓN EXITOSO");
            System.out.println("=================================");
            System.out.println();

            System.out.println("Bienvenido, " + usuarioEncontrado.getNombre());

            usuarioEncontrado.mostrarMenu(scanner, sistema);

        } else {

            System.out.println();
            System.out.println("=================================");
            System.out.println("     ERROR DE AUTENTICACIÓN");
            System.out.println("=================================");
            System.out.println();

            System.out.println("Correo o contraseña incorrectos.");
        }

        scanner.close();
    }
}
