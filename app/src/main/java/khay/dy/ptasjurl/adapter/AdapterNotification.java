package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import khay.dy.ptasjurl.R;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ItemHolder> {

    private Context context;
    private JSONArray array;

    public AdapterNotification(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
