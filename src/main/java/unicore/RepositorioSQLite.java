package unicore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RepositorioSQLite {

    private static final String URL = "jdbc:sqlite:unicore.db";

    static {
        // En algunos entornos, el driver no se auto-registra solo con
        // tenerlo en el classpath. Esto lo fuerza explícitamente.
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de SQLite en el classpath: " + e.getMessage());
        }
    }

    // ==============================
    // INICIALIZACIÓN
    // ==============================

    public void inicializar() {
        String[] sentencias = {
            "PRAGMA foreign_keys = ON;",
            "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, correo TEXT NOT NULL UNIQUE, " +
                "contrasena TEXT NOT NULL, tipo TEXT NOT NULL);",
            "CREATE TABLE IF NOT EXISTS profesores (" +
                "usuario_id INTEGER PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE, " +
                "especialidad TEXT, departamento TEXT);",
            "CREATE TABLE IF NOT EXISTS estudiantes (" +
                "usuario_id INTEGER PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE, " +
                "codigo TEXT, programa TEXT, semestre INTEGER);",
            "CREATE TABLE IF NOT EXISTS administradores (" +
                "usuario_id INTEGER PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE, cargo TEXT);",
            "CREATE TABLE IF NOT EXISTS materias (" +
                "id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, creditos INTEGER, " +
                "profesor_id INTEGER REFERENCES usuarios(id));",
            "CREATE TABLE IF NOT EXISTS matriculas (" +
                "materia_id INTEGER REFERENCES materias(id) ON DELETE CASCADE, " +
                "estudiante_id INTEGER REFERENCES usuarios(id) ON DELETE CASCADE, " +
                "PRIMARY KEY (materia_id, estudiante_id));"
        };

        try (Connection con = DriverManager.getConnection(URL);
             Statement st = con.createStatement()) {

            for (String sql : sentencias) {
                st.execute(sql);
            }

        } catch (SQLException e) {
            System.out.println("Error inicializando la base de datos: " + e.getMessage());
        }
    }

    // ==============================
    // GUARDAR
    // ==============================

    public void guardarUsuario(Usuarios usuario) {
        String sqlUsuario = "INSERT OR REPLACE INTO usuarios (id, nombre, correo, contrasena, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement(sqlUsuario)) {

            ps.setInt(1, usuario.getId());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasenaParaGuardar());
            ps.setString(5, usuario.getClass().getSimpleName());
            ps.executeUpdate();

            guardarDetalleEspecifico(con, usuario);

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el usuario en la base de datos: " + e.getMessage());
        }
    }

    private void guardarDetalleEspecifico(Connection con, Usuarios usuario) throws SQLException {
        if (usuario instanceof Profesor) {
            Profesor p = (Profesor) usuario;
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT OR REPLACE INTO profesores (usuario_id, especialidad, departamento) VALUES (?, ?, ?)")) {
                ps.setInt(1, p.getId());
                ps.setString(2, p.getEspecialidad());
                ps.setString(3, p.getDepartamento());
                ps.executeUpdate();
            }
        } else if (usuario instanceof Estudiante) {
            Estudiante e = (Estudiante) usuario;
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT OR REPLACE INTO estudiantes (usuario_id, codigo, programa, semestre) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, e.getId());
                ps.setString(2, e.getCodigoEstudiante());
                ps.setString(3, e.getPrograma());
                ps.setInt(4, e.getSemestre());
                ps.executeUpdate();
            }
        } else if (usuario instanceof Administrador) {
            Administrador a = (Administrador) usuario;
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT OR REPLACE INTO administradores (usuario_id, cargo) VALUES (?, ?)")) {
                ps.setInt(1, a.getId());
                ps.setString(2, a.getCargo());
                ps.executeUpdate();
            }
        }
    }

    public void guardarMateria(Materia materia) {
        String sql = "INSERT OR REPLACE INTO materias (id, nombre, creditos, profesor_id) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, materia.getId());
            ps.setString(2, materia.getNombre());
            ps.setInt(3, materia.getCreditos());
            ps.setInt(4, materia.getProfesor().getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar la materia en la base de datos: " + e.getMessage());
        }
    }

    public void guardarMatricula(int materiaId, int estudianteId) {
        String sql = "INSERT OR IGNORE INTO matriculas (materia_id, estudiante_id) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, materiaId);
            ps.setInt(2, estudianteId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar la matrícula en la base de datos: " + e.getMessage());
        }
    }

    // ==============================
    // ELIMINAR
    // ==============================

    public void eliminarUsuario(int id) {
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el usuario de la base de datos: " + e.getMessage());
        }
    }

    public void eliminarMateria(int id) {
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement("DELETE FROM materias WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la materia de la base de datos: " + e.getMessage());
        }
    }

    public void eliminarMatricula(int materiaId, int estudianteId) {
        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM matriculas WHERE materia_id = ? AND estudiante_id = ?")) {
            ps.setInt(1, materiaId);
            ps.setInt(2, estudianteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la matrícula de la base de datos: " + e.getMessage());
        }
    }

    // ==============================
    // CARGAR (al iniciar el programa)
    // ==============================

    public void cargarTodoEn(Sistema sistema) {
        try (Connection con = DriverManager.getConnection(URL)) {

            cargarUsuarios(con, sistema);
            cargarMaterias(con, sistema);
            cargarMatriculas(con, sistema);

        } catch (SQLException e) {
            System.out.println("No se pudo cargar la base de datos: " + e.getMessage());
        }
    }

    private void cargarUsuarios(Connection con, Sistema sistema) throws SQLException {
        String sql =
            "SELECT u.id, u.nombre, u.correo, u.contrasena, u.tipo, " +
            "p.especialidad, p.departamento, " +
            "e.codigo, e.programa, e.semestre, " +
            "a.cargo " +
            "FROM usuarios u " +
            "LEFT JOIN profesores p ON u.id = p.usuario_id " +
            "LEFT JOIN estudiantes e ON u.id = e.usuario_id " +
            "LEFT JOIN administradores a ON u.id = a.usuario_id";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String correo = rs.getString("correo");
                String contrasena = rs.getString("contrasena");

                Usuarios usuario = null;

                if ("Profesor".equals(tipo)) {
                    usuario = new Profesor(id, nombre, correo, contrasena,
                        rs.getString("especialidad"), rs.getString("departamento"));
                } else if ("Estudiante".equals(tipo)) {
                    usuario = new Estudiante(id, nombre, correo, contrasena,
                        rs.getString("codigo"), rs.getString("programa"), rs.getInt("semestre"));
                } else if ("Administrador".equals(tipo)) {
                    usuario = new Administrador(id, nombre, correo, contrasena, rs.getString("cargo"));
                }

                if (usuario != null) {
                    sistema.registrarUsuarioSinGuardar(usuario);
                }
            }
        }
    }

    private void cargarMaterias(Connection con, Sistema sistema) throws SQLException {
        String sql = "SELECT id, nombre, creditos, profesor_id FROM materias";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int idProfesor = rs.getInt("profesor_id");
                Usuarios posibleProfesor = sistema.buscarUsuarioPorId(idProfesor);

                if (posibleProfesor instanceof Profesor) {
                    Materia materia = new Materia(
                        rs.getInt("id"), rs.getString("nombre"),
                        rs.getInt("creditos"), (Profesor) posibleProfesor
                    );
                    sistema.registrarMateriaSinGuardar(materia);
                }
            }
        }
    }

    private void cargarMatriculas(Connection con, Sistema sistema) throws SQLException {
        String sql = "SELECT materia_id, estudiante_id FROM matriculas";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Materia materia = sistema.buscarMateriaPorId(rs.getInt("materia_id"));
                Usuarios posibleEstudiante = sistema.buscarUsuarioPorId(rs.getInt("estudiante_id"));

                if (materia != null && posibleEstudiante instanceof Estudiante) {
                    materia.matricular((Estudiante) posibleEstudiante);
                }
            }
        }
    }
}
