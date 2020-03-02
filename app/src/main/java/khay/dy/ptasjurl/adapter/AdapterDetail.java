package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.MyFunction;

public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.ItemHolder> {

    private Context context;
    private JSONArray array;

    public AdapterDetail(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        try {
            JSONObject object = array.getJSONObject(position);
            holder.tv_title.setText(object.getString("title"));
            JSONArray array_item = object.getJSONArray("item");
            for (int i=0;i<array_item.length();i++){
                View view = LayoutInflater.from(context).inflate(R.layout.item_detail,null,false);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);

                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                int width = windowManager.getDefaultDisplay().getWidth();
                view.getLayoutParams().width = width;
                getView(view,array_item.getJSONObject(i));
                holder.flex.addView(view);
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage());
        }
    }
    private void getView(View view,JSONObject object){
        try{
            final TextView tv = view.findViewById(R.id.tv_title);
            tv.setText(object.getString("title")+" : "+object.getString("value"));
        }catch (Exception e){
            Log.e("Err",e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private FlexboxLayout flex;
        private TextView tv_title;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            flex = itemView.findViewById(R.id.flex);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
