package khay.dy.ptasjurl.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;

import static android.content.Context.LOCATION_SERVICE;

public class FragmentMap extends Fragment {

    private final String TAG = "FragmentMap";
    private View root_view;
    private ProgressBar progress;

    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION/*, Manifest.permission.ACCESS_COARSE_LOCATION*/};
    private boolean gps_enabled, googleservice_installed, network_enabled, isCheckGps;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double lat = 11.561545, lng = 104.892521;
    private int DEFAULT_ZOOM = 15, CAMERA_ANIMATE = 800;
    private final int PIN_W = 75;
    private final int PIN_H = 75;
    private boolean isLocation = false;

    private LatLng currentLatLng;

    private LocationManager mLocationManager;
    private Bitmap smallMarker;


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
        accessLocation();
        initView();
    }

    private void initView() {
        findView();
        if (!isLocation)
            getCurrentLocation();
        loadMap();
    }

    private void findView() {
        progress = root_view.findViewById(R.id.progress);
        mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_view));
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        root_view.findViewById(R.id.iv_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLocation) {
                    progress.setVisibility(View.VISIBLE);
                    mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5,
                            5, mLocationListener);
                }else{
                    try {
                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_location);
                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);

                        MarkerOptions marker = new MarkerOptions().position(currentLatLng).title("You").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                        mMap.addMarker(marker);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + "");
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        });

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            currentLatLng = latLng;
            try {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_location);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);

                MarkerOptions marker = new MarkerOptions().position(latLng).title("You").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                mMap.addMarker(marker);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "");
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(cameraUpdate);
            progress.setVisibility(View.GONE);
            isLocation = true;
            return;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    private void initLocation(final JSONArray array) {
        try {
            if (locationManager == null)
                locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            // getting GPS status
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!MyFunction.getInstance().hasPermissions(getContext(), PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) getActivity(), PERMISSIONS, Global.PERMISSION_ALL);
                initMap(array);
            } else {
                initMap(array);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void initMap(final JSONArray array) {
        final ArrayList<LatLng> latLng = new ArrayList<>();
        mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_view));
        try {
            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap map) {

                        if (map != null) {
                            mMap = map;
                            //initialize map
                            try {
                                for (int i = 0; i < array.length(); i++) {
                                    final JSONObject object = array.getJSONObject(i);
                                    try{
                                        latLng.add(new LatLng(object.getDouble("lat"), object.getDouble("long")));
                                    }catch (Exception e){
                                        Log.e("LatLong","Error");
                                    }
                                    final int type = object.getInt(Global.arData[7]);
                                    final String phone = object.getString(Global.arData[26]);
                                    final String price = object.getString(Global.arData[10]);
                                    if(type == 1) {
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_pin1);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);
                                    }else{
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_pin);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);
                                    }
                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                            .position(latLng.get(i))
                                            .title("Price: "+price+"$")
                                            .snippet("Phone: "+phone)
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                                    marker.showInfoWindow();
                                    // MarkerOptions marker = new MarkerOptions().position(latLng.get(i)).title("Phone: "+price+"\nPrice: "+phone).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                                    //mMap.addMarker(marker);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage() + "");
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng.get(0)));
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng.get(0), 15);
                            mMap.animateCamera(cameraUpdate);
                        }
                    }
                });
            } else {
                //new DialogAlertCustom(context, null, getString(R.string.error_occured)).show();
            }
        } catch (Exception e) {
            Log.e("Map ", e.getMessage() + "");
        }
    }

    private void loadMap() {
        try {
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[35], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        final JSONObject object = new JSONObject(response);
                        final JSONArray array = object.getJSONArray(Global.arData[35]);

                        progress.setVisibility(View.GONE);
                        initLocation(array);

                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    loadMap();
                    Log.e("Err", e.getMessage() + "");
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void accessLocation() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(root_view.getContext());

// Setting Dialog Title
            alertDialog.setTitle("GPS is settings");
// Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
// On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

// on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
// Showing Alert Message
            alertDialog.show();
        }
    }

}
