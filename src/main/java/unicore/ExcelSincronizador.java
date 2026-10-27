package unicore;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelSincronizador {

    private static final String RUTA_EXCEL = "unicore.xlsx";
    public void sincronizar(Sistema sistema) {
        try (Workbook libro = new XSSFWorkbook()) {

            escribirHojaUsuarios(libro, sistema.getUsuarios());
            escribirHojaMaterias(libro, sistema.getMaterias());
            escribirHojaMatriculas(libro, sistema.getMaterias());

            try (FileOutputStream salida = new FileOutputStream(RUTA_EXCEL)) {
                libro.write(salida);
            }

            System.out.println("(" + RUTA_EXCEL + " actualizado)");

        } catch (IOException e) {
            System.out.println(
                "No se pudo actualizar " + RUTA_EXCEL
                + ". ¿Está abierto en Excel? Ciérralo e inténtalo de nuevo. Detalle: "
                + e.getMessage()
            );
        } catch (Throwable errorInesperado) {
            // Capturar Throwable (no solo Exception) es poco común y
            // normalmente mala práctica, pero aquí es intencional: la
            // exportación a Excel es un extra "de lujo", nunca debe poder
            // tumbar el programa completo ni bloquear el guardado real en
            // la base de datos. Si algo tan grave como un NoClassDefFoundError
            // ocurre (librería mal instalada, por ejemplo), lo registramos
            // y seguimos, en vez de dejar que la app entera se caiga.
            System.out.println(
                "Fallo inesperado generando " + RUTA_EXCEL
                + " (el resto del sistema sigue funcionando normal): "
                + errorInesperado
            );
        }
    }

    private void escribirHojaUsuarios(Workbook libro, List<Usuarios> usuarios) {
        Sheet hoja = libro.createSheet("Usuarios");

        Row encabezado = hoja.createRow(0);
        String[] titulos = {"ID", "Nombre", "Correo", "Tipo", "Detalle"};
        for (int i = 0; i < titulos.length; i++) {
            encabezado.createCell(i).setCellValue(titulos[i]);
        }

        int numeroFila = 1;
        for (Usuarios u : usuarios) {
            Row fila = hoja.createRow(numeroFila++);
            fila.createCell(0).setCellValue(u.getId());
            fila.createCell(1).setCellValue(u.getNombre());
            fila.createCell(2).setCellValue(u.getCorreo());
            fila.createCell(3).setCellValue(u.getClass().getSimpleName());
            fila.createCell(4).setCellValue(obtenerDetalle(u));
        }

        autoajustarColumnas(hoja, titulos.length);
    }

    private void escribirHojaMaterias(Workbook libro, List<Materia> materias) {
        Sheet hoja = libro.createSheet("Materias");

        Row encabezado = hoja.createRow(0);
        String[] titulos = {"ID", "Nombre", "Creditos", "Profesor", "CantidadEstudiantes"};
        for (int i = 0; i < titulos.length; i++) {
            encabezado.createCell(i).setCellValue(titulos[i]);
        }

        int numeroFila = 1;
        for (Materia m : materias) {
            Row fila = hoja.createRow(numeroFila++);
            fila.createCell(0).setCellValue(m.getId());
            fila.createCell(1).setCellValue(m.getNombre());
            fila.createCell(2).setCellValue(m.getCreditos());
            fila.createCell(3).setCellValue(m.getProfesor().getNombre());
            fila.createCell(4).setCellValue(m.getEstudiantes().size());
        }

        autoajustarColumnas(hoja, titulos.length);
    }

    private void escribirHojaMatriculas(Workbook libro, List<Materia> materias) {
        Sheet hoja = libro.createSheet("Matriculas");

        Row encabezado = hoja.createRow(0);
        String[] titulos = {"MateriaID", "Materia", "EstudianteID", "Estudiante"};
        for (int i = 0; i < titulos.length; i++) {
            encabezado.createCell(i).setCellValue(titulos[i]);
        }

        int numeroFila = 1;
        for (Materia m : materias) {
            for (Estudiante e : m.getEstudiantes()) {
                Row fila = hoja.createRow(numeroFila++);
                fila.createCell(0).setCellValue(m.getId());
                fila.createCell(1).setCellValue(m.getNombre());
                fila.createCell(2).setCellValue(e.getId());
                fila.createCell(3).setCellValue(e.getNombre());
            }
        }

        autoajustarColumnas(hoja, titulos.length);
    }

    private void autoajustarColumnas(Sheet hoja, int cantidadColumnas) {
        for (int i = 0; i < cantidadColumnas; i++) {
            hoja.autoSizeColumn(i);
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
