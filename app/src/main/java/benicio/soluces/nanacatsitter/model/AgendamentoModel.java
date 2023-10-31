package benicio.soluces.nanacatsitter.model;

public class AgendamentoModel {
    String idUsuario;
    int status;
    boolean castrado;
    String horario, data, quaisDias, nomeVeterinario, telefoneVeterinario, ultimaVacinacao, doenca,medicacao, petisco, restricao, brincadeira, comerInadequado, medo, arranha;

    @Override
    public String toString() {

        String isCastrado = castrado ? "sim" : "Não";
        return
                "castrado: " + isCastrado +
                "horario: " + horario + '\n' +
                "data: " + data + '\n' +
                "quaisDias: " + quaisDias + '\n' +
                "nomeVeterinario: " + nomeVeterinario + '\n' +
                "telefoneVeterinario: " + telefoneVeterinario + '\n' +
                "ultimaVacinacao: " + ultimaVacinacao + '\n' +
                "doenca:'" + doenca + '\n' +
                "medicacao: " + medicacao + '\n' +
                "petisco: " + petisco + '\n' +
                "restricao: " + restricao + '\n' +
                "brincadeira: " + brincadeira + '\n' +
                "comerInadequado: " + comerInadequado + '\n' +
                "medo: " + medo + '\n' +
                "arranha: " + arranha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isCastrado() {
        return castrado;
    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getQuaisDias() {
        return quaisDias;
    }

    public void setQuaisDias(String quaisDias) {
        this.quaisDias = quaisDias;
    }

    public String getNomeVeterinario() {
        return nomeVeterinario;
    }

    public void setNomeVeterinario(String nomeVeterinario) {
        this.nomeVeterinario = nomeVeterinario;
    }

    public String getTelefoneVeterinario() {
        return telefoneVeterinario;
    }

    public void setTelefoneVeterinario(String telefoneVeterinario) {
        this.telefoneVeterinario = telefoneVeterinario;
    }

    public String getUltimaVacinacao() {
        return ultimaVacinacao;
    }

    public void setUltimaVacinacao(String ultimaVacinacao) {
        this.ultimaVacinacao = ultimaVacinacao;
    }

    public String getDoenca() {
        return doenca;
    }

    public void setDoenca(String doenca) {
        this.doenca = doenca;
    }

    public String getMedicacao() {
        return medicacao;
    }

    public void setMedicacao(String medicacao) {
        this.medicacao = medicacao;
    }

    public String getPetisco() {
        return petisco;
    }

    public void setPetisco(String petisco) {
        this.petisco = petisco;
    }

    public String getRestricao() {
        return restricao;
    }

    public void setRestricao(String restricao) {
        this.restricao = restricao;
    }

    public String getBrincadeira() {
        return brincadeira;
    }

    public void setBrincadeira(String brincadeira) {
        this.brincadeira = brincadeira;
    }

    public String getComerInadequado() {
        return comerInadequado;
    }

    public void setComerInadequado(String comerInadequado) {
        this.comerInadequado = comerInadequado;
    }

    public String getMedo() {
        return medo;
    }

    public void setMedo(String medo) {
        this.medo = medo;
    }

    public String getArranha() {
        return arranha;
    }

    public void setArranha(String arranha) {
        this.arranha = arranha;
    }
}
