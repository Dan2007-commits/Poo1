package unicore;

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
}