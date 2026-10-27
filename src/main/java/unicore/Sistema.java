package unicore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Sistema {

    private static final String RUTA_USUARIOS = "usuarios.csv";
    private static final String RUTA_MATERIAS = "materias.csv";
    private static final String RUTA_MATRICULAS = "matriculas.csv";

    private List<Usuarios> usuarios;
    private List<Materia> materias;
    private ExcelSincronizador excelSincronizador = new ExcelSincronizador();
    private RepositorioSQLite repositorio = new RepositorioSQLite();

    public Sistema() {
        this.usuarios = new ArrayList<>();
        this.materias = new ArrayList<>();
    }

    // ==============================
    // ARRANQUE: crea las tablas si no existen y carga lo que ya había
    // ==============================

    public void inicializarDesdeBaseDatos() {
        repositorio.inicializar();
        repositorio.cargarTodoEn(this);
        if (!usuarios.isEmpty() || !materias.isEmpty()) {
            exportarTodo();
        }
    }

    // ==============================
    // REGISTRO NORMAL: usuario en tiempo de ejecución (SÍ persiste)
    // ==============================

    public void registrarUsuario(Usuarios usuario) {
        usuarios.add(usuario);
        repositorio.guardarUsuario(usuario);
        exportarTodo();
    }

    public void registrarMateria(Materia materia) {
        materias.add(materia);
        repositorio.guardarMateria(materia);
        exportarTodo();
    }

    // ==============================
    // REGISTRO "SIN GUARDAR": usado SOLO por RepositorioSQLite al cargar
    // datos que ya existían en la base de datos. Si esto llamara a
    // repositorio.guardarUsuario() de nuevo, entraríamos en un bucle tonto
    // de leer y volver a escribir lo mismo que ya estaba guardado.
    // ==============================

    void registrarUsuarioSinGuardar(Usuarios usuario) {
        usuarios.add(usuario);
    }

    void registrarMateriaSinGuardar(Materia materia) {
        materias.add(materia);
    }

    public List<Usuarios> getUsuarios() {
        return usuarios;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public Materia buscarMateriaPorId(int id) {
        for (Materia m : materias) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public Usuarios buscarUsuarioPorId(int id) {
        for (Usuarios u : usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    public int generarNuevoIdUsuario() {
        int maxId = 0;
        for (Usuarios u : usuarios) {
            if (u.getId() > maxId) {
                maxId = u.getId();
            }
        }
        return maxId + 1;
    }

    public int generarNuevoIdMateria() {
        int maxId = 0;
        for (Materia m : materias) {
            if (m.getId() > maxId) {
                maxId = m.getId();
            }
        }
        return maxId + 1;
    }

    public boolean eliminarUsuario(int id) {
        boolean eliminado = usuarios.removeIf(u -> u.getId() == id);
        if (eliminado) {
            repositorio.eliminarUsuario(id);
            exportarTodo();
        }
        return eliminado;
    }

    public boolean eliminarMateria(int id) {
        boolean eliminada = materias.removeIf(m -> m.getId() == id);
        if (eliminada) {
            repositorio.eliminarMateria(id);
            exportarTodo();
        }
        return eliminada;
    }

    public void matricular(Materia materia, Estudiante estudiante) {
        materia.matricular(estudiante);
        repositorio.guardarMatricula(materia.getId(), estudiante.getId());
        exportarTodo();
    }

    public void desmatricular(Materia materia, Estudiante estudiante) {
        materia.desmatricular(estudiante);
        repositorio.eliminarMatricula(materia.getId(), estudiante.getId());
        exportarTodo();
    }

    public void exportarAhora() {
        exportarTodo();
    }

    // ==============================
    // EXPORTACIÓN A EXCEL/CSV (el "espejo" visual, no la fuente de verdad)
    // ==============================

    private void exportarTodo() {
        excelSincronizador.sincronizar(this);
        exportarUsuariosCSV();
        exportarMateriasCSV();
        exportarMatriculasCSV();
    }

    private void exportarUsuariosCSV() {
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(RUTA_USUARIOS), StandardCharsets.UTF_8)) {
            writer.write('\uFEFF');
            writer.write("ID;Nombre;Correo;Tipo;Detalle\n");
            for (Usuarios u : usuarios) {
                writer.write(
                    u.getId() + ";" + u.getNombre() + ";" + u.getCorreo()
                    + ";" + u.getClass().getSimpleName() + ";" + obtenerDetalle(u) + "\n"
                );
            }
        } catch (IOException e) {
            System.out.println("No se pudo actualizar " + RUTA_USUARIOS + ": " + e.getMessage());
        }
    }

    private void exportarMateriasCSV() {
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(RUTA_MATERIAS), StandardCharsets.UTF_8)) {
            writer.write('\uFEFF');
            writer.write("ID;Nombre;Creditos;Profesor;CantidadEstudiantes\n");
            for (Materia m : materias) {
                writer.write(
                    m.getId() + ";" + m.getNombre() + ";" + m.getCreditos()
                    + ";" + m.getProfesor().getNombre() + ";" + m.getEstudiantes().size() + "\n"
                );
            }
        } catch (IOException e) {
            System.out.println("No se pudo actualizar " + RUTA_MATERIAS + ": " + e.getMessage());
        }
    }

    private void exportarMatriculasCSV() {
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(RUTA_MATRICULAS), StandardCharsets.UTF_8)) {
            writer.write('\uFEFF');
            writer.write("MateriaID;Materia;EstudianteID;Estudiante\n");
            for (Materia m : materias) {
                for (Estudiante e : m.getEstudiantes()) {
                    writer.write(
                        m.getId() + ";" + m.getNombre() + ";"
                        + e.getId() + ";" + e.getNombre() + "\n"
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo actualizar " + RUTA_MATRICULAS + ": " + e.getMessage());
        }
    }

    private String obtenerDetalle(Usuarios u) {
        if (u instanceof Profesor) {
            Profesor p = (Profesor) u;
            return "Especialidad: " + p.getEspecialidad() + " / Depto: " + p.getDepartamento();
        } else if (u instanceof Estudiante) {
            Estudiante e = (Estudiante) u;
            return "Codigo: " + e.getCodigoEstudiante()
                + " / Programa: " + e.getPrograma()
                + " / Semestre: " + e.getSemestre();
        } else if (u instanceof Administrador) {
            Administrador a = (Administrador) u;
            return "Cargo: " + a.getCargo();
        }
        return "";
    }
}
