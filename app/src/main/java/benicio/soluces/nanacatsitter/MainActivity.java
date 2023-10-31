package benicio.soluces.nanacatsitter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import benicio.soluces.nanacatsitter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private SharedPreferences preferences;

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
            startActivity(new Intent(getApplicationContext(), AreaPrincipalActivity.class));
        });

        preferences = getSharedPreferences("usuario", MODE_PRIVATE);

        if ( !preferences.getString("idUsuario", "").isEmpty()){
            Toast.makeText(this, "Bem-vindo de volta!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(), AreaPrincipalActivity.class));
        }
    }
}