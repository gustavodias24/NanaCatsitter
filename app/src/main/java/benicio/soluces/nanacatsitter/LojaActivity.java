package benicio.soluces.nanacatsitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import benicio.soluces.nanacatsitter.adapter.AdapterProduto;
import benicio.soluces.nanacatsitter.databinding.ActivityAreaPrincipalBinding;
import benicio.soluces.nanacatsitter.databinding.ActivityLojaBinding;
import benicio.soluces.nanacatsitter.databinding.InserirProdutoLayoutBinding;
import benicio.soluces.nanacatsitter.model.ProdutoModel;

public class LojaActivity extends AppCompatActivity {

    private Dialog dialogCadastroProduto;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refProdutos = database.getReference().child("produtos");
    private ActivityLojaBinding mainBinding;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Boolean isAdm;
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<ProdutoModel> produtos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityLojaBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        editor = preferences.edit();

        isAdm = preferences.getString("idUsuario", "").equals("YWRtaW4=");

        if ( isAdm ){
            mainBinding.addProduto.setVisibility(View.VISIBLE);
        }
        configurarRecyclerView();
        configurarListenerProdutos();
        configurarDialogCadastroProduto();

        mainBinding.addProduto.setOnClickListener( view -> { dialogCadastroProduto.show(); });

        configurarMenu();

        getSupportActionBar().setTitle("Loja");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if ( item.getItemId() == R.id.sair){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            editor.putString("idUsuario", "");
            editor.apply();
        }else if ( item.getItemId() == R.id.carrinho){
            startActivity(new Intent(getApplicationContext(), CarrinhoActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void configurarDialogCadastroProduto() {
        AlertDialog.Builder b  = new AlertDialog.Builder(LojaActivity.this);
        b.setTitle("Produto");
        InserirProdutoLayoutBinding inserirProdutoLayoutBinding = InserirProdutoLayoutBinding.inflate(getLayoutInflater());
        inserirProdutoLayoutBinding.pronto.setOnClickListener( view -> {
            String nome, descri, linkImage;
            float preco;
            String idProduto = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());

            nome = inserirProdutoLayoutBinding.nomeField.getEditText().getText().toString();
            descri = inserirProdutoLayoutBinding.descricaoField.getEditText().getText().toString();
            linkImage = inserirProdutoLayoutBinding.urlField.getEditText().getText().toString();
            preco = Float.parseFloat(
                    inserirProdutoLayoutBinding.valorField.getEditText().getText().toString().replace(",", ".")
            );

            refProdutos.child(idProduto).setValue(
                    new ProdutoModel(nome, descri, idProduto, linkImage, preco)
            ).addOnCompleteListener(task -> {
                if ( task.isSuccessful() ){
                    Toast.makeText(getApplicationContext(), "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    inserirProdutoLayoutBinding.nomeField.getEditText().setText("");
                    inserirProdutoLayoutBinding.descricaoField.getEditText().setText("");
                    inserirProdutoLayoutBinding.urlField.getEditText().setText("");
                    inserirProdutoLayoutBinding.valorField.getEditText().setText("0");
                    dialogCadastroProduto.dismiss();
                }
            });


        });
        b.setView(inserirProdutoLayoutBinding.getRoot());
        dialogCadastroProduto = b.create();
    }

    private void configurarRecyclerView(){
        recyclerProdutos = mainBinding.recyclerProdutos;
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerProdutos.setHasFixedSize(true);
        recyclerProdutos.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        adapterProduto = new AdapterProduto(produtos, getApplicationContext(), isAdm, this, false);
        recyclerProdutos.setAdapter(adapterProduto);

    }

    private void configurarListenerProdutos(){
        refProdutos.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.exists() ){
                    produtos.clear();

                    try{
                        for ( DataSnapshot dado : snapshot.getChildren()){
                            produtos.add(
                                    dado.getValue(ProdutoModel.class)
                            );
                        }
                    }catch (Exception e){
                        Log.d("batata", e.getMessage());
                    }



                    if ( produtos.isEmpty()) { mainBinding.textWarning3.setVisibility(View.VISIBLE);}else{ mainBinding.textWarning3.setVisibility(View.GONE);}
                    adapterProduto.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configurarMenu(){
        mainBinding.acessarAgenda.setOnClickListener(view ->{
            finish();
            startActivity(new Intent(getApplicationContext(), AreaPrincipalActivity.class));
        });
        mainBinding.acessarStore.setOnClickListener( view -> {
            finish();
            startActivity(new Intent(getApplicationContext(), LojaActivity.class));
        });
        mainBinding.acessarPontos.setOnClickListener( view -> {
            finish();
            startActivity(new Intent(getApplicationContext(), MoedasActivity.class));
        });
    }
}