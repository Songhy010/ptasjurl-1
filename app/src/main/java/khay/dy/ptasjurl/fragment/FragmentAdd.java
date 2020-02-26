package khay.dy.ptasjurl.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivitySelectMap;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentAdd extends Fragment {

    private ImageView iv_thum,iv_bed,iv_bath,iv_kit;

    private final String TAG = "FragmentAdd";
    private View root_view;
    private final int PICK_THUM = 1;
    private final int PICK_BED = 2;
    private final int PICK_BATH = 3;
    private final int PICK_KIT = 4;
    private String imgStringThum;
    private String imgStringBed;
    private String imgStringBath;
    private String imgStringKit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add, container, false);
        }
        return root_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        /* TODO set font with current language */
        MyFont.getInstance().setFont(root_view.getContext(), root_view, 2);
    }

    private void findView() {
        iv_thum = root_view.findViewById(R.id.iv_thum);
        iv_bed = root_view.findViewById(R.id.iv_bed);
        iv_bath = root_view.findViewById(R.id.iv_bath);
        iv_kit = root_view.findViewById(R.id.iv_kit);
    }

    private void initView() {
        findView();
        initToolBar();
        initPinMap();
        initPickImage();
        initUpload();
    }

    private void initUpload() {
        root_view.findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(root_view.getContext(), imgStringBath, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPickImage() {
        iv_thum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPickImage(PICK_THUM);
            }
        });
        iv_bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPickImage(PICK_BED);
            }
        });
        iv_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPickImage(PICK_BATH);
            }
        });
        iv_kit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPickImage(PICK_KIT);
            }
        });
    }

    private void initPickImage(final int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    private void initPinMap() {
        root_view.findViewById(R.id.layout_pin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(root_view.getContext(), ActivitySelectMap.class);
            }
        });
    }

    private void initToolBar() {
        final TextView tv_title = root_view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.add_house));
    }

    private String strFromPick(Intent data,ImageView iv){
        try{
            final Uri imageUri = data.getData();
            final InputStream imageStream =getActivity().getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Bitmap bitImg = Bitmap.createScaledBitmap(selectedImage, (int)(selectedImage.getWidth()*0.5),(int)(selectedImage.getHeight()*0.5), true);
            String imgString = Base64.encodeToString(MyFunction.getInstance().getBytesFromBitmap(bitImg),
                    Base64.NO_WRAP);
            Log.e("size",selectedImage.getByteCount()+"");
            Log.e("size",imgString+"");
            iv.setImageBitmap(bitImg);
            return  imgString;
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode){
                case PICK_THUM:
                    imgStringThum = strFromPick(data,iv_thum);
                    break;
                case PICK_BED:
                    imgStringBed = strFromPick(data,iv_bed);
                    break;
                case PICK_BATH:
                    imgStringBath = strFromPick(data,iv_bath);
                    break;
                case PICK_KIT:
                    imgStringKit = strFromPick(data,iv_kit);
                    break;
            }



//            final Uri imageUri = data.getData();
//            final InputStream imageStream =getActivity().getContentResolver().openInputStream(imageUri);
//            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//            Bitmap bitImg = Bitmap.createScaledBitmap(selectedImage, (int)(selectedImage.getWidth()*0.5),(int)(selectedImage.getHeight()*0.5), true);
//            String imgString = Base64.encodeToString(MyFunction.getInstance().getBytesFromBitmap(bitImg),
//                    Base64.NO_WRAP);
//            Log.e("size",selectedImage.getByteCount()+"");
//            Log.e("size",imgString+"");
//            iv_thum.setImageBitmap(bitImg);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(root_view.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}
