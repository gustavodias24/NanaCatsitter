package benicio.soluces.nanacatsitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import benicio.soluces.nanacatsitter.adapter.AdapterProduto;
import benicio.soluces.nanacatsitter.adapter.BannerAdapter;
import benicio.soluces.nanacatsitter.databinding.ActivityAreaUsuairoBinding;
import benicio.soluces.nanacatsitter.databinding.ActivityCriarAgendamentoBinding;
import benicio.soluces.nanacatsitter.model.BannerModel;
import benicio.soluces.nanacatsitter.model.ProdutoModel;


public class AreaUsuairoActivity extends AppCompatActivity {
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<ProdutoModel> produtos = new ArrayList<>();
    private ActivityAreaUsuairoBinding mainBinding;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refbanners = database.getReference().child("banners");
    private DatabaseReference refProdutos = database.getReference().child("produtos");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityAreaUsuairoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().setTitle("Área do usuário");

        configurarBanner();
        mainBinding.irParaOApp.setOnClickListener(
                view -> { finish(); startActivity(new Intent(getApplicationContext(), AreaPrincipalActivity.class));}
        );
        configurarRecyclerView();
        configurarListenerProdutos();
    }

    private void configurarBanner(){
        refbanners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ){
                    List<String> imageUrls = new ArrayList<>();

                    for ( DataSnapshot dado : snapshot.getChildren()){
                        imageUrls.add(Objects.requireNonNull(dado.getValue(BannerModel.class)).getUrlBanner());
                    }

                    ViewPager viewPager = mainBinding.viewPager;
                    BannerAdapter bannerAdapter = new BannerAdapter(getApplicationContext(), imageUrls);
                    viewPager.setAdapter(bannerAdapter);

                    viewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int currentItem = viewPager.getCurrentItem();
                            int totalItems = bannerAdapter.getCount();
                            if (currentItem < totalItems - 1) {
                                viewPager.setCurrentItem(currentItem + 1);
                            } else {
                                viewPager.setCurrentItem(0);
                            }
                            viewPager.postDelayed(this, 3000); // 3000 milissegundos (3 segundos)
                        }
                    }, 3000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void configurarRecyclerView(){
        recyclerProdutos = mainBinding.recyclerProdutos;
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerProdutos.setHasFixedSize(true);
        recyclerProdutos.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        adapterProduto = new AdapterProduto(produtos, getApplicationContext(), false, this, false, true);
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

                    adapterProduto.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}