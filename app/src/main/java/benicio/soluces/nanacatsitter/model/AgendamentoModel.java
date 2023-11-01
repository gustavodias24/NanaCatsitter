package benicio.soluces.nanacatsitter.model;

public class AgendamentoModel {
    String idUsuario;
    String id;
    int status;
    boolean castrado;
    String horario, data, quaisDias, nomeVeterinario, telefoneVeterinario, ultimaVacinacao, doenca,medicacao, petisco, restricao, brincadeira, comerInadequado, medo, arranha;

    @Override
    public String toString() {

        String isCastrado = castrado ? "sim" : "Não";
        String msgStatus = "";
        switch (status){
            case 0:
                msgStatus = "Aguardando Resposta...";
                break;
            case 1:
                msgStatus = "Recusado!";
                break;
            default:
                msgStatus = "Agendado!";
        }
        return
                "Status: " + msgStatus  + '\n'+
                "Castrado: " + isCastrado +
                "Horário: " + horario + '\n' +
                "Data: " + data + '\n' +
                "Quais dias: " + quaisDias + '\n' +
                "Nome do veterinário: " + nomeVeterinario + '\n' +
                "Telefone do veterinário: " + telefoneVeterinario + '\n' +
                "última vacinação: " + ultimaVacinacao + '\n' +
                "Doença:'" + doenca + '\n' +
                "Medicação: " + medicacao + '\n' +
                "Petisco: " + petisco + '\n' +
                "Restrição: " + restricao + '\n' +
                "Brincadeira: " + brincadeira + '\n' +
                "Comida inadequada: " + comerInadequado + '\n' +
                "Medo: " + medo + '\n' +
                "Agreção: " + arranha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
