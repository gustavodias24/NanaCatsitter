package benicio.soluces.nanacatsitter.model;

public class UsuarioModel {
    String login, senha, endereco, nomeGato, idadeGato, whatsapp;
    String token;
    public UsuarioModel(String login, String senha, String endereco, String nomeGato, String idadeGato, String whatsapp) {
        this.login = login;
        this.senha = senha;
        this.endereco = endereco;
        this.nomeGato = nomeGato;
        this.idadeGato = idadeGato;
        this.whatsapp = whatsapp;
    }

    public UsuarioModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNomeGato() {
        return nomeGato;
    }

    public void setNomeGato(String nomeGato) {
        this.nomeGato = nomeGato;
    }

    public String getIdadeGato() {
        return idadeGato;
    }

    public void setIdadeGato(String idadeGato) {
        this.idadeGato = idadeGato;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }
}
