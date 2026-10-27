package unicore;

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
}