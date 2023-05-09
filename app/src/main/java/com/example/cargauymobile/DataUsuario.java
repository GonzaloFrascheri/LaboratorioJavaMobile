package com.example.cargauymobile;

public class DataUsuario {

    private String usuario;
    private String pass;

    private static DataUsuario INSTANCE = null;


    private DataUsuario() {};

    public static DataUsuario getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataUsuario();
        }
        return(INSTANCE);
    }


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
