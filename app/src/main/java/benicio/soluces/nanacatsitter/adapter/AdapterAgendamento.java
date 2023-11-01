package benicio.soluces.nanacatsitter.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import benicio.soluces.nanacatsitter.R;
import benicio.soluces.nanacatsitter.model.AgendamentoModel;

public class AdapterAgendamento extends RecyclerView.Adapter<AdapterAgendamento.MyViewHolder> {
    private SharedPreferences preferences;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refAgendamentos = database.getReference().child("agendamentos");
    Context c;
    List<AgendamentoModel> lista;

    public AdapterAgendamento(Context c, List<AgendamentoModel> lista) {
        this.c = c;
        this.lista = lista;
        this.preferences = c.getSharedPreferences("usuario", MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.exibir_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AgendamentoModel agendamentoModel = lista.get(position);

        holder.infos.setText(
                agendamentoModel.toString()
        );
        if (agendamentoModel.getStatus() == 0){
            if ( preferences.getString("idUsuario", "").equals("YWRtaW4=") ){

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
                });

            }
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView infos;
        LinearLayout layoutBotoes;
        Button cancelar, aceitar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            infos = itemView.findViewById(R.id.textViewInfos);
            layoutBotoes = itemView.findViewById(R.id.layout_adm);
            cancelar = itemView.findViewById(R.id.recusarBtn);
            aceitar = itemView.findViewById(R.id.aceitarBtn);
        }
    }
}
