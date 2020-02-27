package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.MyFunction;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ItemHolder> {

    private JSONArray array;
    private Context context;
    private int layout;

    public AdapterHome(JSONArray array, Context context, int layout) {
        this.array = array;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private CardView card;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            final int height = MyFunction.getInstance().getBannerHeight(context);
            card.getLayoutParams().height = height;
        }
    }
}
