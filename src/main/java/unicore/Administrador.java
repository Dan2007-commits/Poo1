package unicore;

import java.util.Scanner;

public class Administrador extends Usuarios {

    private String cargo;

    public Administrador(
        int id,
        String nombre,
        String correo,
        String contrasena,
        String cargo) {

        super(id, nombre, correo, contrasena);

        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("=== ADMINISTRADOR ===");
        System.out.println("Nombre:" + getNombre());
        System.out.println("Correo:" + getCorreo());
        System.out.println("Cargo:" + cargo);
    }

    @Override
    public void mostrarMenu(Scanner scanner, Sistema sistema) {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Listar todos los usuarios");
            System.out.println("2. Listar todas las materias");
            System.out.println("3. Crear usuario nuevo");
            System.out.println("4. Crear materia nueva");
            System.out.println("5. Matricular estudiante en materia");
            System.out.println("6. Desmatricular estudiante de materia");
            System.out.println("7. Eliminar usuario");
            System.out.println("8. Eliminar materia");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    listarUsuarios(sistema);
                    break;
                case 2:
                    listarMaterias(sistema);
                    break;
                case 3:
                    crearUsuario(scanner, sistema);
                    break;
                case 4:
                    crearMateria(scanner, sistema);
                    break;
                case 5:
                    matricularManual(scanner, sistema);
                    break;
                case 6:
                    desmatricularManual(scanner, sistema);
                    break;
                case 7:
                    eliminarUsuario(scanner, sistema);
                    break;
                case 8:
                    eliminarMateria(scanner, sistema);
                    break;
                case 0:
                    System.out.println("Cerrando sesión...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    // ==============================
    // MÉTODOS PRIVADOS DE APOYO
    // (así el switch de arriba no se convierte en una sopa ilegible)
    // ==============================

    private void listarUsuarios(Sistema sistema) {
        System.out.println();
        for (Usuarios u : sistema.getUsuarios()) {
            System.out.println(
                u.getId() + " - " + u.getNombre()
                + " (" + u.getClass().getSimpleName() + ")"
            );
        }
    }

    private void listarMaterias(Sistema sistema) {
        System.out.println();
        for (Materia m : sistema.getMaterias()) {
            m.mostrarMateria();
            System.out.println();
        }
    }

    private void crearUsuario(Scanner scanner, Sistema sistema) {
        System.out.println();
        System.out.println("¿Qué tipo de usuario quieres crear?");
        System.out.println("1. Profesor");
        System.out.println("2. Estudiante");
        System.out.println("3. Administrador");
        System.out.print("Opción: ");
        int tipo = Integer.parseInt(scanner.nextLine());

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        int nuevoId = sistema.generarNuevoIdUsuario();

        switch (tipo) {
            case 1:
                System.out.print("Especialidad: ");
                String especialidad = scanner.nextLine();
                System.out.print("Departamento: ");
                String departamento = scanner.nextLine();

                Profesor nuevoProfesor = new Profesor(
                    nuevoId, nombre, correo, contrasena, especialidad, departamento
                );
                sistema.registrarUsuario(nuevoProfesor);
                System.out.println("Profesor creado con ID " + nuevoId);
                break;

            case 2:
                System.out.print("Código de estudiante: ");
                String codigo = scanner.nextLine();
                System.out.print("Programa: ");
                String programa = scanner.nextLine();
                System.out.print("Semestre: ");
                int semestre = Integer.parseInt(scanner.nextLine());

                Estudiante nuevoEstudiante = new Estudiante(
                    nuevoId, nombre, correo, contrasena, codigo, programa, semestre
                );
                sistema.registrarUsuario(nuevoEstudiante);
                System.out.println("Estudiante creado con ID " + nuevoId);
                break;

            case 3:
                System.out.print("Cargo: ");
                String cargoNuevo = scanner.nextLine();

                Administrador nuevoAdmin = new Administrador(
                    nuevoId, nombre, correo, contrasena, cargoNuevo
                );
                sistema.registrarUsuario(nuevoAdmin);
                System.out.println("Administrador creado con ID " + nuevoId);
                break;

            default:
                System.out.println("Tipo de usuario inválido, no se creó nada.");
        }
    }

    private void crearMateria(Scanner scanner, Sistema sistema) {
        System.out.println();
        System.out.print("Nombre de la materia: ");
        String nombre = scanner.nextLine();
        System.out.print("Créditos: ");
        int creditos = Integer.parseInt(scanner.nextLine());

        System.out.println();
        System.out.println("Profesores disponibles:");
        for (Usuarios u : sistema.getUsuarios()) {
            if (u instanceof Profesor) {
                System.out.println(u.getId() + " - " + u.getNombre());
            }
        }
        System.out.print("ID del profesor que dictará la materia: ");
        int idProfesor = Integer.parseInt(scanner.nextLine());

        Usuarios posibleProfesor = sistema.buscarUsuarioPorId(idProfesor);

        if (posibleProfesor instanceof Profesor) {
            Profesor profesor = (Profesor) posibleProfesor;
            int nuevoId = sistema.generarNuevoIdMateria();
            Materia nuevaMateria = new Materia(nuevoId, nombre, creditos, profesor);
            sistema.registrarMateria(nuevaMateria);
            System.out.println("Materia creada con ID " + nuevoId);
        } else {
            System.out.println("Ese ID no corresponde a ningún profesor. No se creó la materia.");
        }
    }

    private void matricularManual(Scanner scanner, Sistema sistema) {
        System.out.println();
        System.out.print("ID del estudiante: ");
        int idEstudiante = Integer.parseInt(scanner.nextLine());
        System.out.print("ID de la materia: ");
        int idMateria = Integer.parseInt(scanner.nextLine());

        Usuarios posibleEstudiante = sistema.buscarUsuarioPorId(idEstudiante);
        Materia materia = sistema.buscarMateriaPorId(idMateria);

        if (!(posibleEstudiante instanceof Estudiante)) {
            System.out.println("Ese ID no corresponde a ningún estudiante.");
            return;
        }
        if (materia == null) {
            System.out.println("Esa materia no existe.");
            return;
        }

        materia.matricular((Estudiante) posibleEstudiante);
    }

    private void desmatricularManual(Scanner scanner, Sistema sistema) {
        System.out.println();
        System.out.print("ID del estudiante: ");
        int idEstudiante = Integer.parseInt(scanner.nextLine());
        System.out.print("ID de la materia: ");
        int idMateria = Integer.parseInt(scanner.nextLine());

        Usuarios posibleEstudiante = sistema.buscarUsuarioPorId(idEstudiante);
        Materia materia = sistema.buscarMateriaPorId(idMateria);

        if (!(posibleEstudiante instanceof Estudiante)) {
            System.out.println("Ese ID no corresponde a ningún estudiante.");
            return;
        }
        if (materia == null) {
            System.out.println("Esa materia no existe.");
            return;
        }

        materia.desmatricular((Estudiante) posibleEstudiante);
    }

    private void eliminarUsuario(Scanner scanner, Sistema sistema) {
        System.out.println();
        System.out.print("ID del usuario a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (id == this.getId()) {
            System.out.println("No puedes eliminarte a ti mismo mientras tienes la sesión abierta. Sería incómodo.");
            return;
        }

        boolean eliminado = sistema.eliminarUsuario(id);
        System.out.println(eliminado ? "Usuario eliminado." : "No existe un usuario con ese ID.");
    }

    private void eliminarMateria(Scanner scanner, Sistema sistema) {
        System.out.println();
        System.out.print("ID de la materia a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        boolean eliminada = sistema.eliminarMateria(id);
        System.out.println(eliminada ? "Materia eliminada." : "No existe una materia con ese ID.");
    }
}