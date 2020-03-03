package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;

import khay.dy.ptasjurl.R;

public class AdapterPick extends RecyclerView.Adapter<AdapterPick.ItemHolder> {

    private Context context;
    private ArrayList<Bitmap> mArrayUri = new ArrayList<Bitmap>();

    public AdapterPick(Context context, ArrayList<Bitmap> mArrayUri) {
        this.context = context;
        this.mArrayUri = mArrayUri;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pick,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.iv_img.setImageBitmap(mArrayUri.get(position+1));
    }

    @Override
    public int getItemCount() {
        return mArrayUri.size()-1;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private ImageView iv_img;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
        }
    }
}
