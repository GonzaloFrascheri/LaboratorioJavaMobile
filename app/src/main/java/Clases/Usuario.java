package Clases;

public class Usuario {

    private Integer id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String token;

    private Usuario(Integer id, String nombre, String apellido, String cedula, String token){
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.token = token;
    }

    private static Usuario instancia = null;

    public static Usuario getInstancia(){
        synchronized (Usuario.class){
            Usuario inst = instancia;
            if(inst != null){
                synchronized (Usuario.class){//Se bloquea el acceso, solamente accede la clase Usuario
                    return instancia;
                }
            }
        }
        return null;
    }

    public static Usuario getInstanciaConParametros(Integer id, String nombre, String apellido, String cedula, String token){
        if(instancia != null){
            return instancia;
        }
        instancia = new Usuario(id,nombre,apellido,cedula,token);
        return instancia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //Imprimir el usuario
    public String toString(){
        return "Usuario {" +
                "id = " + id +
                "nombre = " + nombre +
                "apellido = " + apellido +
                "cedular = " + cedula +
                "token = " + token + '\'' +
                '}';
    }
}
