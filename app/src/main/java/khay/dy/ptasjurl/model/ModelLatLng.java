package khay.dy.ptasjurl.model;

import com.google.android.gms.maps.model.LatLng;

public class ModelLatLng {
    private static final ModelLatLng ourInstance = new ModelLatLng();

    public static ModelLatLng getInstance() {
        return ourInstance;
    }

    private ModelLatLng() {
    }

    private LatLng latlng;

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
