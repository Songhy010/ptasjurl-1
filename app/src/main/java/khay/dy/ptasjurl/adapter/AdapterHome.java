package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.my.MyImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityRoomDetail;
import khay.dy.ptasjurl.util.Global;
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
        try {
            int type = 0;
            final JSONObject object = array.getJSONObject(position);
            if (!object.isNull(Global.arData[7]))
                type = Integer.parseInt(object.getString(Global.arData[7]));
            if (type != 3) {
                holder.tv_title.setText(object.getString(Global.arData[8]));
                holder.tv_desc.setText(object.getString(Global.arData[9]));
                holder.tv_price.setText(object.getString(Global.arData[10]));
            } else {
                holder.tv_title.setText("Promotion");
                holder.tv_desc.setText("Promotion");
                holder.tv_price.setText("Promotion");
            }
            MyImageLoader.getInstance().setImage(holder.iv_thum, object.getString(Global.arData[11]), null, 0, 0, position, R.drawable.img_loading, R.drawable.img_loading);

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyFunction.getInstance().openActivity(context, ActivityRoomDetail.class);
                }
            });
        } catch (Exception e) {
            Log.e("Err", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return array == null ? 0 : array.length();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private CardView card;
        private ImageView iv_thum;
        private TextView tv_title, tv_price, tv_desc;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            final int height = MyFunction.getInstance().getBannerHeight(context);
            card.getLayoutParams().height = height;

            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);

            iv_thum = itemView.findViewById(R.id.iv_thum);
        }
    }
}
