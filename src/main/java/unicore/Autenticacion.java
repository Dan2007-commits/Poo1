package unicore;

import java.util.List;

public class Autenticacion {

    public Usuarios iniciarSesion(String correo, String contrasena, List<Usuarios> usuarios) {

        for (Usuarios usuario : usuarios) {

            if (usuario.getCorreo().equals(correo)
                    && usuario.verificarContrasena(contrasena)) {

                return usuario;
            }
        }

        return null;
    }
}