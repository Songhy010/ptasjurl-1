package khay.dy.ptasjurl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.my.MyImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;

public class AdapterBanner extends PagerAdapter {

    ViewGroup view;
    private Context mContext;
    private List<String> mListImage;
    private JSONArray array;

    public AdapterBanner(Context mContext, List<String> mListImage,JSONArray array) {
        this.mContext = mContext;
        this.mListImage = mListImage;
        this.array = array;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        view = (ViewGroup) object;
        super.setPrimaryItem(container, position, object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        try{
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = (ViewGroup) inflater.inflate(R.layout.item_banner, container, false);
            MyFont.getInstance().setFont(mContext,view,4);
            final ImageView iv_book = view.findViewById(R.id.iv_banner);
            final JSONObject object= array.getJSONObject(position);
            MyImageLoader.getInstance().setImage(iv_book, object.getString(Global.arData[13]), null, 0, 0, position, R.drawable.img_loading, R.drawable.img_loading);

            //iv_book.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_sample));
            container.addView(view);
        }catch (Exception e){
            Log.e("Err",e.getMessage());
        }

        return view;
    }


}
