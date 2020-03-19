package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Global;

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
        try {
            final JSONObject object = array.getJSONObject(position);
            holder.tv_title.setText(object.getString(Global.arData[8]));
            holder.tv_desc.setText(object.getString(Global.arData[37]));
            holder.tv_date.setText(object.getString(Global.arData[36]));
        }catch (Exception e){
            Log.e("Err",e.getMessage()+"");
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tv_title,tv_desc,tv_date;
        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
