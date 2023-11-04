package benicio.soluces.nanacatsitter.model;

import android.annotation.SuppressLint;

public class ProdutoModel {
    public ProdutoModel() {
    }

    String nome, descricao, id, urlImage;
    float preco;


    public ProdutoModel(String nome, String descricao, String id, String urlImage, float preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.id = id;
        this.urlImage = urlImage;
        this.preco = preco;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return  nome + '\n' +
                descricao + '\n'+
                String.format("R$ %.2f", preco);
    }
}
