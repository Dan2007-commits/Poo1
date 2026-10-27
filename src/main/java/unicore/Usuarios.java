package unicore;

import java.util.Scanner;

public abstract class Usuarios {

    private int id;
    private String nombre;
    private String correo;
    private String contrasena;

    public Usuarios(int id, String nombre, String correo, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    // Ya no exponemos la contraseña, solo respondemos "sí" o "no" a un intento.
    public boolean verificarContrasena(String intento) {
        return this.contrasena.equals(intento);
    }

    // OJO: sin modificador de acceso = "package-private". Solo clases DENTRO
    // del paquete "unicore" pueden llamar esto (como RepositorioSQLite).
    // Cualquier código externo al paquete sigue sin poder leer la contraseña.
    // Advertencia honesta: esto la guarda tal cual en la base de datos, sin
    // hashear. Para un proyecto universitario puede pasar, pero en un
    // sistema real esto se hace con BCrypt o similar, nunca en texto plano.
    String getContrasenaParaGuardar() {
        return contrasena;
    }

    // Cada subclase está OBLIGADA a implementar esto. Sin excusas.
    public abstract void mostrarInformacion();

    // Cada tipo de usuario tiene su propio menú al iniciar sesión.
    public abstract void mostrarMenu(Scanner scanner, Sistema sistema);

    // Sin esto, dos estudiantes con el mismo id se consideran "distintos"
    // aunque sean el mismo, y cosas como contains() en listas fallan.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Usuarios)) return false;
        Usuarios otro = (Usuarios) obj;
        return this.id == otro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
