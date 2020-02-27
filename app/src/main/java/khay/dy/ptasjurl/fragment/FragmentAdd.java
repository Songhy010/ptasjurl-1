package khay.dy.ptasjurl.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivitySelectMap;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.models.model_latlng;

public class FragmentAdd extends Fragment {

    private ImageView iv_thum, iv_bed, iv_bath, iv_kit;
    private TextView tv_address;

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

    @Override
    public void onResume() {
        super.onResume();
        if (model_latlng.getInstance().getLatLng() != null)
            tv_address.setText(getAddress(model_latlng.getInstance().getLatLng()));
    }

    private String getAddress(LatLng latLng) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(root_view.getContext(), Locale.getDefault());
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            return address;
        } catch (Exception e) {
            Log.e("Err", e.getMessage());
        }
        return "";
    }

    private void findView() {
        iv_thum = root_view.findViewById(R.id.iv_thum);
        iv_bed = root_view.findViewById(R.id.iv_bed);
        iv_bath = root_view.findViewById(R.id.iv_bath);
        iv_kit = root_view.findViewById(R.id.iv_kit);
        tv_address = root_view.findViewById(R.id.tv_address);
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
                HashMap<String,String> map = new HashMap<>();
                map.put("avatar",imgStringThum);
                map.put("img_name","ok");
                MyFunction.getInstance().requestString(Request.Method.POST, "http://192.168.1.6:3000/api/client/upload",map, new VolleyCallback() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(root_view.getContext(), response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.e("Err",e.getMessage()+"");
                    }
                });
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

    private String strFromPick(Intent data, ImageView iv) {
        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Bitmap bitImg = Bitmap.createScaledBitmap(selectedImage, (int) (selectedImage.getWidth() * 0.5), (int) (selectedImage.getHeight() * 0.5), true);
            String imgString = Base64.encodeToString(MyFunction.getInstance().getBytesFromBitmap(bitImg),
                    Base64.NO_WRAP);
            Log.e("size", selectedImage.getByteCount() + "");
            Log.e("size", imgString + "");
            iv.setImageBitmap(bitImg);
            return imgString;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case PICK_THUM:
                    imgStringThum = strFromPick(data, iv_thum);
                    break;
                case PICK_BED:
                    imgStringBed = strFromPick(data, iv_bed);
                    break;
                case PICK_BATH:
                    imgStringBath = strFromPick(data, iv_bath);
                    break;
                case PICK_KIT:
                    imgStringKit = strFromPick(data, iv_kit);
                    break;
            }
            Toast.makeText(root_view.getContext(), "asdasd", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(root_view.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}
