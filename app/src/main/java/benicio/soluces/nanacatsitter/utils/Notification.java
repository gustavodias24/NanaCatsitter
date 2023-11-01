package benicio.soluces.nanacatsitter.utils;

public class Notification {
    String msg;
    String usuario;

    public Notification() {
    }

    public Notification(String msg, String usuario) {
        this.msg = msg;
        this.usuario = usuario;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
