package benicio.soluces.nanacatsitter.adapter;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import benicio.soluces.nanacatsitter.R;

public class BannerAdapter  extends PagerAdapter {
    private Context mContext;
    private List<String> mImageUrls;

    public BannerAdapter(Context context, List<String> mImageUrls) {
        this.mImageUrls = mImageUrls;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.banner_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        // Carregar a imagem da URL usando o Glide
        Picasso.get().load(Uri.parse(mImageUrls.get(position))).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}