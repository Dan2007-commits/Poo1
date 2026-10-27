package unicore;

import java.util.Scanner;

public class Estudiante extends Usuarios {

    private String codigoEstudiante;
    private String programa;
    private int semestre;

    public Estudiante(
        int id,
        String nombre,
        String correo,
        String contrasena,
        String codigoEstudiante,
        String programa,
        int semestre) {
        super(id, nombre, correo, contrasena);

        this.codigoEstudiante = codigoEstudiante;
        this.programa = programa;
        this.semestre = semestre;
    }

    public String getCodigoEstudiante() {
        return codigoEstudiante;
    }

    public String getPrograma() {
        return programa;
    }

    public int getSemestre() {
        return semestre;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("==== ESTUDIANTE =====");
        System.out.println("Nombre:" + getNombre());
        System.out.println("Correo:" + getCorreo());
        System.out.println("Codigo:" + codigoEstudiante);
        System.out.println("Programa:" + programa);
        System.out.println("Semestre:" + semestre);
    }

    @Override
    public void mostrarMenu(Scanner scanner, Sistema sistema) {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== MENÚ ESTUDIANTE ===");
            System.out.println("1. Ver materias disponibles");
            System.out.println("2. Matricularme en una materia");
            System.out.println("3. Ver mis materias matriculadas");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    System.out.println();
                    for (Materia m : sistema.getMaterias()) {
                        System.out.println(
                            m.getId() + " - " + m.getNombre()
                            + " (" + m.getCreditos() + " créditos, dicta: "
                            + m.getProfesor().getNombre() + ")"
                        );
                    }
                    break;

                case 2:
                    System.out.print("ID de la materia: ");
                    int idMateria = Integer.parseInt(scanner.nextLine());
                    Materia materia = sistema.buscarMateriaPorId(idMateria);

                    if (materia != null) {
                        materia.matricular(this);
                    } else {
                        System.out.println("No existe una materia con ese ID.");
                    }
                    break;

                case 3:
                    System.out.println();
                    System.out.println("Materias matriculadas:");
                    for (Materia m : sistema.getMaterias()) {
                        if (m.getEstudiantes().contains(this)) {
                            System.out.println(" - " + m.getNombre());
                        }
                    }
                    break;

                case 0:
                    System.out.println("Cerrando sesión...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }
}