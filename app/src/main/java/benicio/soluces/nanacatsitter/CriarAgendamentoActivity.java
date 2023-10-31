package benicio.soluces.nanacatsitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import benicio.soluces.nanacatsitter.databinding.ActivityAreaPrincipalBinding;
import benicio.soluces.nanacatsitter.databinding.ActivityCriarAgendamentoBinding;
import benicio.soluces.nanacatsitter.model.AgendamentoModel;

public class CriarAgendamentoActivity extends AppCompatActivity {

    private ActivityCriarAgendamentoBinding mainBinding;
    private SharedPreferences preferences;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refAgendamentos = database.getReference().child("agendamentos");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityCriarAgendamentoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        
        // confirurar toolbar
        getSupportActionBar().setTitle("Criar agendamento");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainBinding.pronto.setOnClickListener( view -> {
            AgendamentoModel agendamentoModel = new AgendamentoModel();
            
            agendamentoModel.setStatus(0);
            agendamentoModel.setIdUsuario(preferences.getString("idUsuario", ""));
            
            agendamentoModel.setHorario(
                    mainBinding.horarioField.getEditText().getText().toString()
            );
            agendamentoModel.setData(
                    mainBinding.dataField.getEditText().getText().toString()
            );
            agendamentoModel.setQuaisDias(
                    mainBinding.quaisDiasField.getEditText().getText().toString()
            );
            agendamentoModel.setNomeVeterinario(
                    mainBinding.nomeVeterinarioField.getEditText().getText().toString()
            );
            agendamentoModel.setTelefoneVeterinario(
                    mainBinding.telefoneVeterinRioField.getEditText().getText().toString()
            );
            agendamentoModel.setCastrado(
                    mainBinding.radiocastrado.isChecked()
            );
            agendamentoModel.setUltimaVacinacao(
                    mainBinding.ultimaVacinacaoField.getEditText().getText().toString()
            );
            agendamentoModel.setDoenca(
                    mainBinding.doencaField.getEditText().getText().toString()
            );
            agendamentoModel.setMedicacao(
                    mainBinding.medicacaoField.getEditText().getText().toString()
            );
            agendamentoModel.setPetisco(
                    mainBinding.petiscoField.getEditText().getText().toString()
            );
            agendamentoModel.setRestricao(
                    mainBinding.restricaoField.getEditText().getText().toString()
            );
            agendamentoModel.setBrincadeira(
                    mainBinding.brincadeiraField.getEditText().getText().toString()
            );
            agendamentoModel.setComerInadequado(
                    mainBinding.comerInadequadoField.getEditText().getText().toString()
            );
            agendamentoModel.setMedo(
                    mainBinding.medoField.getEditText().getText().toString()
            );
            agendamentoModel.setArranha(
                    mainBinding.arranhaField.getEditText().getText().toString()
            );
            
            cadastrarAgendamento(agendamentoModel);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){finish();}
        return super.onOptionsItemSelected(item);
    }
    
    private void cadastrarAgendamento( AgendamentoModel agendamentoModel){
        Toast.makeText(this, "Aguarde...", Toast.LENGTH_SHORT).show();
        refAgendamentos.push().setValue(
                agendamentoModel
        ).addOnCompleteListener(task -> {
            if ( task.isSuccessful() ){
                finish();
                Toast.makeText(CriarAgendamentoActivity.this, "Agendamento realizado, aguarde aprovação!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(CriarAgendamentoActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

}