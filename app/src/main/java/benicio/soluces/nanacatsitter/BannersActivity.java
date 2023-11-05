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
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import benicio.soluces.nanacatsitter.adapter.AdapterBanner;
import benicio.soluces.nanacatsitter.databinding.ActivityAreaPrincipalBinding;
import benicio.soluces.nanacatsitter.databinding.ActivityBannersBinding;
import benicio.soluces.nanacatsitter.databinding.AdicionarBannerBinding;
import benicio.soluces.nanacatsitter.model.AgendamentoModel;
import benicio.soluces.nanacatsitter.model.BannerModel;

public class BannersActivity extends AppCompatActivity {

    private Dialog d;
    private ActivityBannersBinding mainBinding;
    private RecyclerView r;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refbanners = database.getReference().child("banners");
    private List<BannerModel> banners = new ArrayList<>();
    private AdapterBanner adapterBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityBannersBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Banners");
        configurarBanners();
        configurarListenerBanner();
        configurarDialog();

        mainBinding.addAgendamento.setOnClickListener( view -> {
            d.show();
        });
    }

    private void configurarDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(BannersActivity.this);
        b.setTitle("Adicionar um banner");
        AdicionarBannerBinding adicionarBannerBinding = AdicionarBannerBinding.inflate(getLayoutInflater());
        adicionarBannerBinding.inserir.setOnClickListener( view -> {
            String id = UUID.randomUUID().toString();
            refbanners.child(id).setValue(
                    new BannerModel(
                            id,
                            adicionarBannerBinding.urlField.getEditText().getText().toString()
                            )).addOnCompleteListener(task -> {
                                if ( task.isSuccessful() ){
                                    Toast.makeText(BannersActivity.this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                                    adicionarBannerBinding.urlField.getEditText().setText("");
                                    d.dismiss();
                                }
                            });
        });
        b.setView(adicionarBannerBinding.getRoot());
        d = b.create();

    }

    private void configurarListenerBanner(){
        refbanners.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ){
                    banners.clear();

                    for ( DataSnapshot dado : snapshot.getChildren()){
                        BannerModel bannerModel = dado.getValue(BannerModel.class);
                        banners.add(bannerModel);
                    }

                    adapterBanner.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void configurarBanners() {
        r = mainBinding.recyclerBanners;
        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        adapterBanner = new AdapterBanner(banners,getApplicationContext());
        r.setAdapter(adapterBanner);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}