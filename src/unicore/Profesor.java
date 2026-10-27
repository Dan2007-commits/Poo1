package unicore;

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
}