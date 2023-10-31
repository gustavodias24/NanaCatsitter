package benicio.soluces.nanacatsitter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.soluces.nanacatsitter.R;
import benicio.soluces.nanacatsitter.model.AgendamentoModel;

public class AdapterAgendamento extends RecyclerView.Adapter<AdapterAgendamento.MyViewHolder> {
    Context c;
    List<AgendamentoModel> lista;

    public AdapterAgendamento(Context c, List<AgendamentoModel> lista) {
        this.c = c;
        this.lista = lista;
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
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView infos;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            infos = itemView.findViewById(R.id.textViewInfos);
        }
    }
}
