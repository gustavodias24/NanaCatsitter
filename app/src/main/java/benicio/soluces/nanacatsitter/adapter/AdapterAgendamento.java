package benicio.soluces.nanacatsitter.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import benicio.soluces.nanacatsitter.R;
import benicio.soluces.nanacatsitter.model.AgendamentoModel;
import benicio.soluces.nanacatsitter.model.UsuarioModel;

public class AdapterAgendamento extends RecyclerView.Adapter<AdapterAgendamento.MyViewHolder> {
    private SharedPreferences preferences;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refAgendamentos = database.getReference().child("agendamentos");
    private DatabaseReference refUsuarios = database.getReference().child("usuarios");
    Context c;
    List<AgendamentoModel> lista;

    Boolean isAdmin;

    public AdapterAgendamento(Context c, List<AgendamentoModel> lista) {
        this.c = c;
        this.lista = lista;
        this.preferences = c.getSharedPreferences("usuario", MODE_PRIVATE);
        this.isAdmin = preferences.getString("idUsuario", "").equals("YWRtaW4=");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.exibir_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AgendamentoModel agendamentoModel = lista.get(position);

        if( agendamentoModel.getStatus() == 2 && isAdmin){
            holder.concluir.setVisibility(View.VISIBLE);
        }

        holder.infos.setText(
                agendamentoModel.toString()
        );
        if (agendamentoModel.getStatus() == 0){
            if ( isAdmin ){

                holder.layoutBotoes.setVisibility(
                        View.VISIBLE
                );

                holder.cancelar.setOnClickListener( view -> {
                    agendamentoModel.setStatus(1);
                    refAgendamentos.child(agendamentoModel.getId()).setValue(agendamentoModel);
                    holder.layoutBotoes.setVisibility(
                            View.GONE
                    );
                });

                holder.aceitar.setOnClickListener( view -> {
                    agendamentoModel.setStatus(2);
                    refAgendamentos.child(agendamentoModel.getId()).setValue(agendamentoModel);
                    holder.layoutBotoes.setVisibility(
                            View.GONE
                    );

                    adicioanarMoeda(agendamentoModel.getIdUsuario());

                    holder.concluir.setVisibility(View.VISIBLE);

                });
            }
        }

        holder.concluir.setOnClickListener( view -> {
            agendamentoModel.setStatus(3);
            refAgendamentos.child(agendamentoModel.getId()).setValue(agendamentoModel).addOnCompleteListener(
                    task -> {
                        if ( task.isSuccessful() ){
                            Toast.makeText(c, "Concluído", Toast.LENGTH_SHORT).show();
                            holder.concluir.setVisibility(View.GONE);
                        }
                    }
            );

        });

    }

    private void adicioanarMoeda(String idUsuario){

        refUsuarios.child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ){
                    UsuarioModel usuarioModel = snapshot.getValue(UsuarioModel.class);
                    usuarioModel.setMoedas(
                            usuarioModel.getMoedas() + 1
                    );

                    refUsuarios.child(idUsuario).setValue(
                            usuarioModel
                    ).addOnCompleteListener(task -> {
                        if ( task.isSuccessful() ){
                            Toast.makeText(c, "Moeda adicionada!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView infos;
        LinearLayout layoutBotoes;
        Button cancelar, aceitar, concluir;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            infos = itemView.findViewById(R.id.textViewInfos);
            layoutBotoes = itemView.findViewById(R.id.layout_adm);
            cancelar = itemView.findViewById(R.id.recusarBtn);
            aceitar = itemView.findViewById(R.id.aceitarBtn);
            concluir = itemView.findViewById(R.id.concluir);
        }
    }
}
