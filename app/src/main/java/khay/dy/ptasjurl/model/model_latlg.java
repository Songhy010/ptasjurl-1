package khay.dy.ptasjurl.model;

import com.google.android.gms.maps.model.LatLng;

public class model_latlg {
    private static final model_latlg ourInstance = new model_latlg();

    public static model_latlg getInstance() {
        return ourInstance;
    }

    private model_latlg() {
    }

    private LatLng latlng;

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
