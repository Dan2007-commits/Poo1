package unicore;

import java.util.ArrayList;
import java.util.List;

public class Materia {

    private int id;
    private String nombre;
    private int creditos;
    private Profesor profesor;
    private List<Estudiante> estudiantes;

    public Materia(
        int id,
        String nombre,
        int creditos,
        Profesor profesor) {

        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
        this.profesor = profesor;
        this.estudiantes = new ArrayList<>();
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

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void matricular(Estudiante estudiante) {
        if (estudiantes.contains(estudiante)) {
            System.out.println(estudiante.getNombre() + " ya está matriculado en " + nombre);
            return;
        }
        estudiantes.add(estudiante);
        System.out.println(estudiante.getNombre() + " fue matriculado en " + nombre);
    }

    public boolean desmatricular(Estudiante estudiante) {
        boolean seEncontraba = estudiantes.remove(estudiante);
        if (seEncontraba) {
            System.out.println(estudiante.getNombre() + " fue retirado de " + nombre);
        } else {
            System.out.println(estudiante.getNombre() + " no estaba matriculado en " + nombre);
        }
        return seEncontraba;
    }

    public void mostrarMateria() {
        System.out.println("=== MATERIA ===");
        System.out.println("ID:" + id);
        System.out.println("Materia:" + nombre);
        System.out.println("Creditos:" + creditos);
        System.out.println("Profesor:" + profesor.getNombre());
        System.out.println("Estudiantes matriculados: " + estudiantes.size());
        for (Estudiante e : estudiantes) {
            System.out.println(" - " + e.getNombre());
        }
    }
}