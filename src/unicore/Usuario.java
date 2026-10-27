package unicore;

public class Usuarios{
    private int id;
    private string nombre;
    private string correo;
    private string contraseña;

    public Usuario(int, id, string nombre, 
    string correo, string contraseña)
    
    {
    this.id = id;
    this.nombre = nombre;
    this.correo = correo;
    this.contraseña = contraseña;
    }

public int getId() {
    return id;
    }

public string getNombre(){
    return nombre;
} 

public string getCorreo (){
    return correo;
}

public string getContraseña(){
    return contraseña;
}

public void mostrar informacion(){
    System.out.println("ID: "+ id);
    System.out.println("Nombre:" + nombre);
    System.out.println("Correo:" + correo);
}
}
