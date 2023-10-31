package benicio.soluces.nanacatsitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.Base64;

import benicio.soluces.nanacatsitter.databinding.ActivityCadastroBinding;
import benicio.soluces.nanacatsitter.databinding.ActivityMainBinding;
import benicio.soluces.nanacatsitter.model.UsuarioModel;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding mainBinding;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUsuairos = database.getReference().child("usuarios");

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // confirurar toolbar
        getSupportActionBar().setTitle("Cadastro");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainBinding.pronto.setOnClickListener( view -> {
            String login, senha, endereco, idadeGato, nomeGato, whatsapp;
            
            login = mainBinding.loginField.getEditText().getText().toString();
            senha = mainBinding.senhaField.getEditText().getText().toString();
            idadeGato = mainBinding.idadeField.getEditText().getText().toString();
            nomeGato = mainBinding.nomeGatoField.getEditText().getText().toString();
            whatsapp = mainBinding.contatoField.getEditText().getText().toString();
            endereco = mainBinding.enderecoField.getEditText().getText().toString();
            
            if(
                    login.isEmpty() || senha.isEmpty() || idadeGato.isEmpty() || nomeGato.isEmpty()
                    || whatsapp.isEmpty() || endereco.isEmpty()
            ){
                Toast.makeText(this, "Preencha todas as informações!", Toast.LENGTH_SHORT).show();
            }else{
                cadastrarDatabase(
                        new UsuarioModel(login, senha, endereco, nomeGato, idadeGato, whatsapp)
                );
            }
            
        });

        preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){finish();}
        return super.onOptionsItemSelected(item);
    }
    
    private void cadastrarDatabase(UsuarioModel usuarioModel){
        String id_usuario = Base64.getEncoder().encodeToString(usuarioModel.getLogin().getBytes());
        refUsuairos.child(id_usuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ){
                    Toast.makeText(getApplicationContext(), "Login já cadastrado tente outro.", Toast.LENGTH_SHORT).show();
                }else{
                    refUsuairos.child(id_usuario).setValue(usuarioModel);
                    editor.putString("idUsuario", id_usuario);
                    editor.apply();
                    finish();
                    startActivity(new Intent(getApplicationContext(), AreaPrincipalActivity.class));
                    Toast.makeText(getApplicationContext(), "Cadastro feito com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}