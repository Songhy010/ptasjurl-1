package khay.dy.ptasjurl.models;

import com.google.android.gms.maps.model.LatLng;

public class model_latlng {
    private static final model_latlng ourInstance = new model_latlng();

    public static model_latlng getInstance() {
        return ourInstance;
    }

    private model_latlng() {
    }

    private LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
