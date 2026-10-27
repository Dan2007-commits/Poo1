package unicore;

public class Materia {

    private int id;
    private String nombre;
    private int creditos;
    private Profesor profesor;

    public Materia(
        int id,
        String nombre,
        int creditos,
        Profesor profesor) {

        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
        this.profesor = profesor;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void mostrarMateria() {
        System.out.println("=== MATERIA ===");
        System.out.println("ID:" + id);
        System.out.println("Materia:" + nombre);
        System.out.println("Creditos:" + creditos);
        System.out.println("Profesor:" + profesor.getNombre());
    }
}