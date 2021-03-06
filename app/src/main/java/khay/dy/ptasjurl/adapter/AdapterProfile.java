package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityAboutUs;
import khay.dy.ptasjurl.activity.ActivityContactUs;
import khay.dy.ptasjurl.activity.ActivityDonate;
import khay.dy.ptasjurl.activity.ActivityLogin;
import khay.dy.ptasjurl.activity.ActivityMyHouse;
import khay.dy.ptasjurl.activity.ActivityProfileView;
import khay.dy.ptasjurl.activity.ActivitySetting;
import khay.dy.ptasjurl.activity.ActivityTermAndCondition;
import khay.dy.ptasjurl.listener.AlertListenner;
import khay.dy.ptasjurl.util.MyFunction;

public class AdapterProfile extends RecyclerView.Adapter<AdapterProfile.ItemHolder> {

    private Context context;
    private String[] menu;
    private int[] icon = {R.drawable.ic_profile,R.drawable.img_my_house,R.drawable.img_donate,
            R.drawable.ic_about,R.drawable.ic_contact,R.drawable.ic_term,R.drawable.ic_setting};
    private Class<?>[] activity = {ActivityProfileView.class,
            ActivityMyHouse.class,
            ActivityDonate.class,
            ActivityAboutUs.class,
            ActivityContactUs.class,
            ActivityTermAndCondition.class,
            ActivitySetting.class};

    public AdapterProfile(Context context, String[] menu) {
        this.context = context;
        this.menu = menu;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        holder.tv_menu.setText(menu[position]);
        holder.iv_icon.setImageDrawable(context.getResources().getDrawable(icon[position]));
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position ==0 || position ==1){
                    if(!MyFunction.getInstance().isHistory(context)){
                        MyFunction.getInstance().alertMessage(context, context.getString(R.string.login_required), new AlertListenner() {
                            @Override
                            public void onSubmit() {
                                MyFunction.getInstance().openActivityForResult(context,ActivityLogin.class,null,position);
                            }
                        },1);
                    }else{
                        MyFunction.getInstance().openActivity(context,activity[position]);
                    }
                }else{
                    MyFunction.getInstance().openActivity(context,activity[position]);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menu.length;
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tv_menu;
        private LinearLayout linear;
        private ImageView iv_icon;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            linear = itemView.findViewById(R.id.linear);
            tv_menu = itemView.findViewById(R.id.tv_menu);
            iv_icon = itemView.findViewById(R.id.iv_icon);
        }
    }
}
