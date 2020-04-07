package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.AccesoriesListener;
import khay.dy.ptasjurl.util.Global;

public class AdapterAccessories extends RecyclerView.Adapter<AdapterAccessories.ItemHolder> {
    private JSONArray array;
    private Context context;
    private AccesoriesListener accesoriesListener;

    public AdapterAccessories(JSONArray array, Context context,AccesoriesListener accesoriesListener) {
        this.array = array;
        this.context = context;
        this.accesoriesListener = accesoriesListener;
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
            holder.checkBox.setText(object.getString(Global.arData[8]));
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    try{
                        if(b){
                            accesoriesListener.onChecked(object.getString("id"));
                        }else{
                            accesoriesListener.onUnCheck(object.getString("id"));
                        }
                    }catch (Exception e){
                        Log.e("Err",e.getMessage()+"");
                    }
                }
            });
        }catch (Exception e){
            Log.e("Err",e.getMessage());
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
