package unicore;

public class Main {

    public static void main(String[] args) {

        Profesor profesor = new Profesor(
            1,
            "Carlos Rodriguez",
            "carlos@unicore.com",
            "1234",
            "Programación",
            "Ingenieria de sistemas"
        );

        Estudiante estudiante = new Estudiante(
            2,
            "Dann",
            "Dann@unicore.com",
            "1234",
            "2026001",
            "Ingenieria de sistemas",
            1
        );

        Administrador administrador = new Administrador(
            3,
            "Laura Gomez",
            "Laura@unicore.com",
            "123",
            "Administrador académico"
        );

        Materia programacion = new Materia(
            1,
            "Programación orientada a objetos",
            3,
            profesor
        );

        estudiante.mostrarInformacion();

        System.out.println();

        profesor.mostrarInformacion();

        System.out.println();

        administrador.mostrarInformacion();

        System.out.println();

        programacion.mostrarMateria();
    }
}