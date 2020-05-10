package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.my.MyImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityRoomDetail;
import khay.dy.ptasjurl.model.ModelItem;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;

public class AdapterItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public Context context;

    private ArrayList<ModelItem> modelItemList;

    private OnLoadMoreListener onLoadMoreListener;

    private boolean isMoreLoading = true;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public AdapterItem(Context context, OnLoadMoreListener onLoadMoreListener) {
        this.context = context;
        this.onLoadMoreListener = onLoadMoreListener;
        modelItemList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {

        return modelItemList.get(position) != null ? VIEW_ITEM : VIEW_PROG;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false));
        }
    }

    public void showLoading() {
        if (isMoreLoading && modelItemList != null && onLoadMoreListener != null) {
            isMoreLoading = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    modelItemList.add(null);
                    notifyItemInserted(modelItemList.size() - 1);
                    onLoadMoreListener.onLoadMore();
                }
            });
        }
    }

    public void setMore(boolean isMore) {
        this.isMoreLoading = isMore;
    }

    public void dismissLoading() {
        if (modelItemList != null && modelItemList.size() > 0) {
            modelItemList.remove(modelItemList.size() - 1);
            notifyItemRemoved(modelItemList.size());
        }
    }

    public void addAll(List<ModelItem> lst) {
        modelItemList.clear();
        modelItemList.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<ModelItem> lst) {
        int sizeInit = modelItemList.size();
        modelItemList.addAll(lst);
        notifyItemRangeChanged(sizeInit, modelItemList.size());
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentViewHolder) {
            final ModelItem singleModelItem = (ModelItem) modelItemList.get(position);
            try {
                if (singleModelItem.getType().equals("2")) {
                    ((StudentViewHolder) holder).layout.setVisibility(View.VISIBLE);
                    ((StudentViewHolder) holder).layout1.setVisibility(View.GONE);
                    ((StudentViewHolder) holder).tv_title.setText("Room for Rent");
                    MyFunction.getInstance().displayHtmlInText(((StudentViewHolder) holder).tv_desc, singleModelItem.getPhone1());
                    ((StudentViewHolder) holder).tv_price.setText(singleModelItem.getPrice() + "$");
                    ((StudentViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                final HashMap<String, String> map = new HashMap<>();
                                map.put(Global.arData[7], singleModelItem.getType());
                                map.put(Global.arData[44], singleModelItem.getId());
                                MyFunction.getInstance().openActivity(context, ActivityRoomDetail.class, map);
                            } catch (Exception e) {
                                Log.e("Err", e.getMessage() + "");
                            }
                        }
                    });
                } else if (singleModelItem.getType().equals("1")) {
                    ((StudentViewHolder) holder).layout.setVisibility(View.GONE);
                    ((StudentViewHolder) holder).layout1.setVisibility(View.VISIBLE);
                    ((StudentViewHolder) holder).tv_title1.setText("Flat for rent");
                    MyFunction.getInstance().displayHtmlInText(((StudentViewHolder) holder).tv_desc1, singleModelItem.getPhone1());
                    ((StudentViewHolder) holder).tv_price1.setText(singleModelItem.getPrice() + "$");
                    ((StudentViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                final HashMap<String, String> map = new HashMap<>();
                                map.put(Global.arData[7], singleModelItem.getType());
                                map.put(Global.arData[44], singleModelItem.getId());
                                MyFunction.getInstance().openActivity(context, ActivityRoomDetail.class, map);
                            } catch (Exception e) {
                                Log.e("Err", e.getMessage() + "");
                            }
                        }
                    });
                } else {
                    ((StudentViewHolder) holder).layout.setVisibility(View.GONE);
                    ((StudentViewHolder) holder).layout1.setVisibility(View.GONE);
                }
                MyImageLoader.getInstance().setImage(((StudentViewHolder) holder).iv_thum, singleModelItem.getThumbnail(), null, 0, 0, position, R.drawable.img_loading, R.drawable.img_loading);


            } catch (Exception e) {
                Log.e("Err", e.getMessage());
            }
        }
    }


    @Override
    public int getItemCount() {
        return modelItemList.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private CardView card;
        private ImageView iv_thum;
        private TextView tv_title, tv_price, tv_desc;
        private RelativeLayout layout;

        private TextView tv_title1, tv_price1, tv_desc1;
        private RelativeLayout layout1;

        public StudentViewHolder(View v) {
            super(v);
            card = itemView.findViewById(R.id.card);
            final int height = MyFunction.getInstance().getBannerHeight(context);
            card.getLayoutParams().height = height;
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            iv_thum = itemView.findViewById(R.id.iv_thum);
            layout = itemView.findViewById(R.id.layout);

            tv_desc1 = itemView.findViewById(R.id.tv_desc1);
            tv_title1 = itemView.findViewById(R.id.tv_title1);
            tv_price1 = itemView.findViewById(R.id.tv_price1);
            layout1 = itemView.findViewById(R.id.layout1);
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }
}