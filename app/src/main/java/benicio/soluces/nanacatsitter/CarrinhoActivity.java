package benicio.soluces.nanacatsitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import benicio.soluces.nanacatsitter.adapter.AdapterProduto;
import benicio.soluces.nanacatsitter.databinding.ActivityCarrinhoBinding;
import benicio.soluces.nanacatsitter.databinding.ActivityLojaBinding;
import benicio.soluces.nanacatsitter.model.ProdutoModel;
import benicio.soluces.nanacatsitter.model.UsuarioModel;
import benicio.soluces.nanacatsitter.utils.CarrinhoSave;

public class CarrinhoActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    int moedas;
    private ActivityCarrinhoBinding mainBinding;
    private RecyclerView r;
    private List<ProdutoModel> produtos = new ArrayList<>();
    private AdapterProduto adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUsuarios = database.getReference().child("usuarios");
    
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityCarrinhoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        editor = preferences.edit();
        
        getSupportActionBar().setTitle("Carrinho");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        configurarRecyclerView();

        configurarInfos();

        mainBinding.limpar.setOnClickListener( view -> {
            produtos.clear();
            adapter.notifyDataSetChanged();
            CarrinhoSave.saveProdutos(getApplicationContext(), produtos);
            configurarInfos();
        });

        mainBinding.comprar.setOnClickListener( view -> {
            enviarParaWhatsapp();
        });
    }

    @SuppressLint("DefaultLocale")
    private void configurarInfos() {



        refUsuarios.child(preferences.getString("idUsuario", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ){
                    float totalPagar = 0.0f;
                    float valorDesconto = 0.0f;

                    for (ProdutoModel produtoModel : CarrinhoSave.loadProdutos(getApplicationContext())){
                        totalPagar += produtoModel.getPreco();
                    }


                    moedas = snapshot.getValue(UsuarioModel.class).getMoedas();
                    valorDesconto = totalPagar - ( totalPagar * moedas/100 );
                    mainBinding.textInfos.setText(String.format("Valor da compra: R$ %.2f\nVocê tem %d%% de desconto\nTotal a pagar: R$ %.2f", totalPagar, moedas, valorDesconto));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void configurarRecyclerView() {
        r = mainBinding.recyclerProdutos;
        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        produtos.addAll(CarrinhoSave.loadProdutos(getApplicationContext()));

        if ( produtos.isEmpty()) {
            mainBinding.limpar.setVisibility(View.GONE);
        }
        adapter = new AdapterProduto(produtos, getApplication(), false, this, true);
        r.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if ( item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("DefaultLocale")
    public void enviarParaWhatsapp(){
        StringBuilder text = new StringBuilder("Olá, gostaria de comprar os seguintes itens:\n");

        float valorCompra = 0.0f;
        float valorDesconto = 0.0f;

        for ( ProdutoModel produtoModel : produtos){
            text.append(
                    String.format("%s R$ %.2f", produtoModel.getNome(), produtoModel.getPreco())
            ).append("\n");

            valorCompra += produtoModel.getPreco();
        }
        String metodo = mainBinding.metodoPagamentoField.getEditText().getText().toString();

        valorDesconto = valorCompra - ( valorCompra * moedas/100 );
        text.append("Método: " + metodo).append("\n");
        text.append("Desconto: " + moedas + " porcento").append("\n");
        text.append(String.format("Valor da compra: R$ %.2f", valorCompra)).append("\n");
        text.append(String.format("Valor com desconto: R$ %.2f", valorDesconto)).append("\n");
        String urlZap = "https://wa.me/5587981572689?text=" + text ;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlZap)));
    }


}