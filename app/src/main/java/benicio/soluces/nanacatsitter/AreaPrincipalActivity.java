package benicio.soluces.nanacatsitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import benicio.soluces.nanacatsitter.adapter.AdapterAgendamento;
import benicio.soluces.nanacatsitter.adapter.BannerAdapter;
import benicio.soluces.nanacatsitter.databinding.ActivityAreaPrincipalBinding;
import benicio.soluces.nanacatsitter.databinding.ActivityCadastroBinding;
import benicio.soluces.nanacatsitter.databinding.AdicionarBannerBinding;
import benicio.soluces.nanacatsitter.databinding.TrocarSenhaLayoutBinding;
import benicio.soluces.nanacatsitter.model.AgendamentoModel;
import benicio.soluces.nanacatsitter.model.BannerModel;
import benicio.soluces.nanacatsitter.model.UsuarioModel;

public class AreaPrincipalActivity extends AppCompatActivity {
    private Boolean listeneTodos = true;
    private Dialog dialogTrocarSenha, dialogFiltro;
    private ActivityAreaPrincipalBinding mainBinding;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RecyclerView r;
    private AdapterAgendamento adapter;
    private List<AgendamentoModel> listaAgendamento = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refAgendamentos = database.getReference().child("agendamentos");
    private DatabaseReference refUsuarios = database.getReference().child("usuarios");
    private DatabaseReference refbanners = database.getReference().child("banners");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityAreaPrincipalBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        editor = preferences.edit();

        // confirurar toolbar
        getSupportActionBar().setTitle("Agendamentos");

        mainBinding.addAgendamento.setOnClickListener( view -> {
            startActivity(new Intent(getApplicationContext(), CriarAgendamentoActivity.class));
        });

        // configuração da listagem
        configurarRecycler();
        configurarListenerAgendamento();

        if( preferences.getString("idUsuario", "").equals("YWRtaW4=") ){
            mainBinding.addAgendamento.setVisibility(View.GONE);
            mainBinding.contatobtn.setVisibility(View.GONE);
            mainBinding.menuAdm.setVisibility(View.VISIBLE);
        }

        configurarDialogTrocarSenha();
        mainBinding.trocarSenha.setOnClickListener( view -> {
            dialogTrocarSenha.show();
        });
        mainBinding.banners.setOnClickListener( view ->{
            startActivity(new Intent(getApplicationContext(), BannersActivity.class));
        });
        mainBinding.produtos.setOnClickListener( view -> {
            startActivity(new Intent(getApplicationContext(), LojaActivity.class));
        });
        mainBinding.filtrar.setOnClickListener( view -> {
            dialogFiltro.show();
        });

        configurarMenu();
        configurarBanner();
        configurarDialogFiltro();
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
                if ( listeneTodos ){
                    listaAgendamento.clear();

                    for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                        AgendamentoModel agendamento = dataSnapshot.getValue(AgendamentoModel.class);
                        assert agendamento != null;
                        if ( preferences.getString("idUsuario", "").equals("YWRtaW4=") ||
                                agendamento.getIdUsuario().equals(preferences.getString("idUsuario", ""))){
                            if ( agendamento.getStatus() != 1){
                                listaAgendamento.add(agendamento);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();

                    if ( !listaAgendamento.isEmpty()){
                        mainBinding.textWarning.setVisibility(View.GONE);
                    }else{
                        mainBinding.textWarning.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mainBinding.contatobtn.setOnClickListener(view -> {
            startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://wa.me/5587981572689")
                    )
            );
        });

        mainBinding.saudacaotext.setText(
                saudacaoDoDia()
        );
    }

    private String saudacaoDoDia() {
        Calendar cal = Calendar.getInstance();
        int hora = cal.get(Calendar.HOUR_OF_DAY);

        String saudacao = "";
        if (hora >= 6 && hora < 12) {
            saudacao = "Miaw dia!";
        } else if (hora >= 12 && hora < 18) {
            saudacao = "Miaw tarde!";
        } else {
            saudacao = "Miaw noite!";
        }

        return saudacao;
    }

    private void configurarDialogTrocarSenha(){
        AlertDialog.Builder b = new AlertDialog.Builder(AreaPrincipalActivity.this);
        TrocarSenhaLayoutBinding trocarSenhaLayoutBinding = TrocarSenhaLayoutBinding.inflate(getLayoutInflater());
        b.setTitle("Trocar a senha de Login");

        trocarSenhaLayoutBinding.alterar.setOnClickListener(view -> {
            String login, senha;

            login = trocarSenhaLayoutBinding.loginField.getEditText().getText().toString();
            senha = trocarSenhaLayoutBinding.novaSenhaField.getEditText().getText().toString();

            String idUsuario = Base64.getEncoder().encodeToString(
                    login.getBytes()
            );

            refUsuarios.child(idUsuario).get().addOnCompleteListener(task -> {
                if ( task.isSuccessful() && task.getResult().exists()){
                    UsuarioModel usuarioModel = task.getResult().getValue(UsuarioModel.class);
                    usuarioModel.setSenha(senha);
                    refUsuarios.child(idUsuario).setValue(usuarioModel).addOnCompleteListener(task1 -> {
                        if ( task1.isSuccessful()){
                            Toast.makeText(AreaPrincipalActivity.this, "Senha trocada com sucesso!", Toast.LENGTH_SHORT).show();
                            trocarSenhaLayoutBinding.loginField.getEditText().setText("");
                            trocarSenhaLayoutBinding.novaSenhaField.getEditText().setText("");
                            dialogTrocarSenha.dismiss();
                        }

                    });

                }else{
                    Toast.makeText(this, "Usuário não encontrado ou erro de conexão!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        b.setView(trocarSenhaLayoutBinding.getRoot());
        dialogTrocarSenha = b.create();

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

    private  void configurarDialogFiltro(){
        AlertDialog.Builder b = new AlertDialog.Builder(AreaPrincipalActivity.this);

        b.setTitle("Filtro");
        b.setMessage("Clique no agende para ver todos novamente.");
        AdicionarBannerBinding binding = AdicionarBannerBinding.inflate(getLayoutInflater());

        EditText filtro = binding.urlField.getEditText();
        binding.urlField.setHint("Filtro");
        Button button = binding.inserir;
        button.setText("filtrar");

        button.setOnClickListener( view -> {
            String filtroString = filtro.getText().toString();

            if ( filtroString.isEmpty() ){
                listeneTodos = true;
            }else{
                listeneTodos = false;
                refAgendamentos.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if ( !listeneTodos ){
                            listaAgendamento.clear();

                            for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                                AgendamentoModel agendamento = dataSnapshot.getValue(AgendamentoModel.class);
                                assert agendamento != null;
                                if ( agendamento.getNomeUsuario().toLowerCase().trim().equals(filtroString) ){
                                    assert agendamento != null;
                                    if ( preferences.getString("idUsuario", "").equals("YWRtaW4=") ||
                                            agendamento.getIdUsuario().equals(preferences.getString("idUsuario", ""))){
                                        if ( agendamento.getStatus() != 1){
                                            listaAgendamento.add(agendamento);
                                        }
                                    }
                                }
                            }

                            adapter.notifyDataSetChanged();

                            if ( !listaAgendamento.isEmpty()){
                                Toast.makeText(AreaPrincipalActivity.this, listaAgendamento.size() + " resultados encontrados", Toast.LENGTH_SHORT).show();
                                mainBinding.textWarning.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(AreaPrincipalActivity.this, "Nada encontrado com filtro " + filtroString, Toast.LENGTH_SHORT).show();
                                mainBinding.textWarning.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        b.setView(binding.getRoot());
        dialogFiltro = b.create();

    }

}