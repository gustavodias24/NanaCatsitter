package benicio.soluces.nanacatsitter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import benicio.soluces.nanacatsitter.R;
import benicio.soluces.nanacatsitter.model.BannerModel;

public class AdapterBanner extends RecyclerView.Adapter<AdapterBanner.MyViewHolder> {

    List<BannerModel> banners;
    Context c;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refbanners = database.getReference().child("banners");

    public AdapterBanner(List<BannerModel> banners, Context c) {
        this.banners = banners;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.exibir_banner_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BannerModel bannerModel = banners.get(position);

        Picasso.get().load(bannerModel.getUrlBanner()).into(holder.bannerImage);
        holder.deleteButton.setOnClickListener( view -> {
            refbanners.child(bannerModel.getId()).setValue(null).addOnCompleteListener(task -> {
                if ( task.isSuccessful()){
                    Toast.makeText(c, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {

        ImageView bannerImage;
        Button deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.imageBanner);
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }
}
