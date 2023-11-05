package benicio.soluces.nanacatsitter.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import benicio.soluces.nanacatsitter.R;
import benicio.soluces.nanacatsitter.databinding.InserirProdutoLayoutBinding;
import benicio.soluces.nanacatsitter.model.ProdutoModel;
import benicio.soluces.nanacatsitter.utils.CarrinhoSave;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refProdutos = database.getReference().child("produtos");
    List<ProdutoModel> produtos;
    Context c;
    Boolean isAdm;

    Activity a;

    Boolean isCarrinho;

    Boolean noButon;

    public AdapterProduto(List<ProdutoModel> produtos, Context c, Boolean isAdm, Activity a, Boolean isCarrinho, Boolean noButon) {
        this.produtos = produtos;
        this.c = c;
        this.isAdm = isAdm;
        this.a = a;
        this.isCarrinho = isCarrinho;
        this.noButon = noButon;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.exibir_produto, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProdutoModel produtoModel = produtos.get(position);


        try{
            Picasso.get().load(produtoModel.getUrlImage()).into(holder.imageProduto);
        }catch (Exception e){
            Log.d("batata", e.getMessage());
        }
        holder.infoProduto.setText(produtoModel.toString());

        if ( isAdm ){
            holder.layoutAdm.setVisibility(View.VISIBLE);
            holder.carrinho.setVisibility(View.GONE);

            holder.excluir.setOnClickListener( view -> {
                refProdutos.child(produtoModel.getId()).setValue(null).addOnCompleteListener(task -> {
                    if ( task.isSuccessful())
                        Toast.makeText(c, "Excluído com sucesso", Toast.LENGTH_SHORT).show();
                });
            });

            holder.editar.setOnClickListener( view -> {
                configurarDialogEdicaoProduto(produtoModel);
            });
        }else{
            holder.layoutAdm.setVisibility(View.GONE);
            holder.carrinho.setVisibility(View.VISIBLE);
            
            holder.carrinho.setOnClickListener( view -> {
                List<ProdutoModel> carrinho = CarrinhoSave.loadProdutos(c);
                carrinho.add(produtoModel);
                CarrinhoSave.saveProdutos(c, carrinho);
                Toast.makeText(c, "Adicionado no carrinho com sucesso!", Toast.LENGTH_SHORT).show();
            });
        }

        if ( isCarrinho ){
            holder.layoutAdm.setVisibility(View.GONE);
            holder.carrinho.setVisibility(View.GONE);
        }

        if ( noButon ){
            holder.layoutAdm.setVisibility(View.GONE);
            holder.carrinho.setVisibility(View.GONE);
            holder.carrinho.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void configurarDialogEdicaoProduto(ProdutoModel produtoModel) {
        AlertDialog.Builder b  = new AlertDialog.Builder(a);
        b.setTitle("Editar "+ produtoModel.getNome());
        InserirProdutoLayoutBinding inserirProdutoLayoutBinding = InserirProdutoLayoutBinding.inflate(a.getLayoutInflater());
        Objects.requireNonNull(inserirProdutoLayoutBinding.nomeField.getEditText()).setText(produtoModel.getNome());
        Objects.requireNonNull(inserirProdutoLayoutBinding.descricaoField.getEditText()).setText(produtoModel.getDescricao());
        Objects.requireNonNull(inserirProdutoLayoutBinding.urlField.getEditText()).setText(produtoModel.getUrlImage());
        Objects.requireNonNull(inserirProdutoLayoutBinding.valorField.getEditText()).setText(produtoModel.getPreco() + "");
        inserirProdutoLayoutBinding.pronto.setOnClickListener( view -> {
            String nome, descri, linkImage;
            float preco;

            nome = inserirProdutoLayoutBinding.nomeField.getEditText().getText().toString();
            descri = inserirProdutoLayoutBinding.descricaoField.getEditText().getText().toString();
            linkImage = inserirProdutoLayoutBinding.urlField.getEditText().getText().toString();
            preco = Float.parseFloat(
                    inserirProdutoLayoutBinding.valorField.getEditText().getText().toString().replace(",", ".")
            );

            refProdutos.child(produtoModel.getId()).setValue(
                    new ProdutoModel(nome, descri, produtoModel.getId(), linkImage, preco)
            ).addOnCompleteListener(task -> {
                if ( task.isSuccessful() ){
                    Toast.makeText(c, "Produto editado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });


        });
        b.setView(inserirProdutoLayoutBinding.getRoot());
        b.create().show();
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {

        ImageView imageProduto;
        TextView infoProduto;
        Button carrinho, editar, excluir;
        LinearLayout layoutAdm;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProduto = itemView.findViewById(R.id.imageProduto);
            infoProduto = itemView.findViewById(R.id.textViewInfosProduto);
            carrinho = itemView.findViewById(R.id.adicionar_carrinho);
            editar = itemView.findViewById(R.id.editarProduto);
            excluir = itemView.findViewById(R.id.excluirProduto);
            layoutAdm = itemView.findViewById(R.id.layout_adm_produto);
        }
    }
}
