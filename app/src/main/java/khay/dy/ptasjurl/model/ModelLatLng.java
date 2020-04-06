package khay.dy.ptasjurl.model;

import com.google.android.gms.maps.model.LatLng;

public class ModelLatLng {
    private static final ModelLatLng ourInstance = new ModelLatLng();

    public static ModelLatLng getInstance() {
        return ourInstance;
    }

    private ModelLatLng() {
    }

    private LatLng latlng = new LatLng(11.5564,104.9282);

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
