package com.example.cargauymobile;

public class Chofer {

    private String usuario;

    private String pass;

    private static Chofer INSTANCE = null;

    private Chofer(){};

    public static Chofer getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Chofer();
        }
        return INSTANCE;
    }

    public String getUsuario() { return usuario; }

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
