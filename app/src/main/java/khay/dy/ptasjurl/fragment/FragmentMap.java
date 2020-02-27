package khay.dy.ptasjurl.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentMap extends Fragment {

    private final String TAG = "FragmentMap";
    private View root_view;

    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION/*, Manifest.permission.ACCESS_COARSE_LOCATION*/};
    private boolean gps_enabled, googleservice_installed, network_enabled, isCheckGps;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double lat = 11.561545, lng = 104.892521;
    private int DEFAULT_ZOOM = 15, CAMERA_ANIMATE = 800;
    private final int PIN_W = 75;
    private final int PIN_H = 75;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_map, container, false);
        }
        return root_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        /* TODO set font with current language */
       // MyFont.getInstance().setFont(root_view.getContext(),root_view,2);
    }

    private void findView(){
        mapFragment = ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map_view));
    }

    private void initView(){
        findView();
        initToolBar();
        initLocation();
    }
    private void initToolBar() {
        final TextView tv_title = root_view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.map));
    }
    private void initLocation(){
        try{
            if (locationManager == null)
                locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!MyFunction.getInstance().hasPermissions(getContext(), PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity)getActivity(), PERMISSIONS, Global.PERMISSION_ALL);
                initMap();
            } else {
                initMap();
            }
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }
    private void initMap() {
        mapFragment = ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map_view));
        try{
            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap map) {

                        if (map != null) {
                            mMap = map;
                            //initialize map
                            LatLng latLng = new LatLng(lat, lng);

                            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.img_pin);
                            Bitmap b=bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);

                            MarkerOptions marker = new MarkerOptions().position(latLng).title("Aide et Action").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                            mMap.addMarker(marker);

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM), CAMERA_ANIMATE, null);

                        }
                    }
                });
            } else {
                //new DialogAlertCustom(context, null, getString(R.string.error_occured)).show();
            }
        }catch (Exception e){
            Log.e("Map ",e.getMessage()+"");
        }
    }


}
