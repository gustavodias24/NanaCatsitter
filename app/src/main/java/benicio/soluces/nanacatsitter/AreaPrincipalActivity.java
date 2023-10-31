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

import benicio.soluces.nanacatsitter.adapter.AdapterAgendamento;
import benicio.soluces.nanacatsitter.databinding.ActivityAreaPrincipalBinding;
import benicio.soluces.nanacatsitter.databinding.ActivityCadastroBinding;
import benicio.soluces.nanacatsitter.model.AgendamentoModel;

public class AreaPrincipalActivity extends AppCompatActivity {

    private ActivityAreaPrincipalBinding mainBinding;
    private SharedPreferences preferences;
    private RecyclerView r;
    AdapterAgendamento adapter;
    private List<AgendamentoModel> listaAgendamento = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refAgendamentos = database.getReference().child("agendamentos");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityAreaPrincipalBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("usuario", MODE_PRIVATE);

        // confirurar toolbar
        getSupportActionBar().setTitle("Agendamentos");

        mainBinding.addAgendamento.setOnClickListener( view -> {
            startActivity(new Intent(getApplicationContext(), CriarAgendamentoActivity.class));
        });

        // configuração da listagem
        configurarRecycler();
        configurarListenerAgendamento();
    }

    public void configurarRecycler(){
        r = mainBinding.recyclerAgendamento;
        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        adapter = new AdapterAgendamento(getApplicationContext(), listaAgendamento);
        r.setAdapter(adapter);
    }

    private void configurarListenerAgendamento(){
        refAgendamentos.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaAgendamento.clear();

                for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AgendamentoModel agendamento = dataSnapshot.getValue(AgendamentoModel.class);
                    assert agendamento != null;
                    if ( agendamento.getIdUsuario().equals(preferences.getString("idUsuario", ""))){
                        listaAgendamento.add(agendamento);
                    }
                }

                adapter.notifyDataSetChanged();

                if ( !listaAgendamento.isEmpty()){
                    mainBinding.textWarning.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}