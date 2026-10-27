package unicore;

import java.util.Scanner;

public class Profesor extends Usuarios {

    private String especialidad;
    private String departamento;

    public Profesor(
        int id,
        String nombre,
        String correo,
        String contrasena,
        String especialidad,
        String departamento) {

        super(id, nombre, correo, contrasena);

        this.especialidad = especialidad;
        this.departamento = departamento;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getDepartamento() {
        return departamento;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== PROFESOR ===");
        System.out.println("Nombre:" + getNombre());
        System.out.println("Correo:" + getCorreo());
        System.out.println("Especialidad:" + especialidad);
        System.out.println("Departamento:" + departamento);
    }

    @Override
    public void mostrarMenu(Scanner scanner, Sistema sistema) {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== MENÚ PROFESOR ===");
            System.out.println("1. Ver mis materias");
            System.out.println("2. Ver detalle de una de mis materias");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    System.out.println();
                    for (Materia m : sistema.getMaterias()) {
                        if (m.getProfesor().equals(this)) {
                            System.out.println(m.getId() + " - " + m.getNombre());
                        }
                    }
                    break;

                case 2:
                    System.out.print("ID de la materia: ");
                    int idMateria = Integer.parseInt(scanner.nextLine());
                    Materia materia = sistema.buscarMateriaPorId(idMateria);

                    if (materia != null && materia.getProfesor().equals(this)) {
                        materia.mostrarMateria();
                    } else {
                        System.out.println("Esa materia no existe o no es tuya.");
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