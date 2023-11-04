package benicio.soluces.nanacatsitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Base64;

import benicio.soluces.nanacatsitter.databinding.ActivityMainBinding;
import benicio.soluces.nanacatsitter.model.UsuarioModel;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUsuairos = database.getReference().child("usuarios");
    private ActivityMainBinding mainBinding;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //configurar a logo
        Picasso.get().load(R.raw.logo).into(mainBinding.logo);


        mainBinding.cadastrar.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
        });

        mainBinding.entrar.setOnClickListener( view -> {
            String login, senha;

            login = mainBinding.loginField.getEditText().getText().toString();
            senha = mainBinding.senhaField.getEditText().getText().toString();

            if ( login.isEmpty() || senha.isEmpty() ){
                Toast.makeText(this, "Preencha todos os dados.", Toast.LENGTH_SHORT).show();
            }else{
                fazerLogin(
                        Base64.getEncoder().encodeToString(login.getBytes()),
                        senha
                );
            }

        });

        preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        editor = preferences.edit();

        if ( !preferences.getString("idUsuario", "").isEmpty()){
            Toast.makeText(this, "Bem-vindo de volta!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(), AreaPrincipalActivity.class));
        }

        mainBinding.esqueiSenha.setOnClickListener( view -> {
            startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://wa.me/55879881572689?text=Ol%C3%A1,%20gostaria%20de%20trocar%20minha%20senha%20do%20aplicativo%20Nana%20Cat%20Sitter")
                    )
            );
        });
    }

    private void fazerLogin(String idUsuario, String senha){

        refUsuairos.child(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ){
                    UsuarioModel usuarioModel = snapshot.getValue(UsuarioModel.class);
                    if (usuarioModel.getSenha().equals(senha)){
                        Toast.makeText(MainActivity.this, "Bem-vindo de volta!", Toast.LENGTH_SHORT).show();
                        finish();
                        editor.putString("idUsuario", idUsuario);
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), AreaPrincipalActivity.class));
                    }else{
                        Toast.makeText(MainActivity.this, "Senha inválida!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Login não encontrado!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}