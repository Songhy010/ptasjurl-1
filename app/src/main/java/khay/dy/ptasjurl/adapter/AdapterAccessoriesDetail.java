package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Global;

public class AdapterAccessoriesDetail extends RecyclerView.Adapter<AdapterAccessoriesDetail.ItemHolder> {

    private Context context;
    private JSONArray array;

    public AdapterAccessoriesDetail(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_check,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        try{
            final JSONObject object = array.getJSONObject(position);
            holder.checkBox.setText(object.getString(Global.arData[76]));
            holder.checkBox.setChecked(true);
            holder.checkBox.setEnabled(false);
        }catch (Exception e){
            Log.e("Err",e.getMessage()+"");
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private CheckBox checkBox;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check);
        }
    }
}
