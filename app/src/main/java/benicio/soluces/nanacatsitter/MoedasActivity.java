package benicio.soluces.nanacatsitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import benicio.soluces.nanacatsitter.databinding.ActivityLojaBinding;
import benicio.soluces.nanacatsitter.databinding.ActivityMoedasBinding;
import benicio.soluces.nanacatsitter.model.UsuarioModel;

public class MoedasActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUsuarios = database.getReference().child("usuarios");

    private ActivityMoedasBinding mainBinding;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMoedasBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().setTitle("Moedas");

        preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        editor = preferences.edit();
        configurarMenu();
        configurarInfos();
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

    private void configurarInfos(){
        refUsuarios.child(preferences.getString("idUsuario", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ){
                    UsuarioModel usuarioModel = snapshot.getValue(UsuarioModel.class);
                    mainBinding.textInfos.setText(
                            String.format(
                                    "Você tem %d Moedas\n%d%% de desconto em compras.", usuarioModel.getMoedas(), usuarioModel.getMoedas()
                                    )
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}