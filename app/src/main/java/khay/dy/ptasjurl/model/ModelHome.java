package khay.dy.ptasjurl.model;

import org.json.JSONObject;

public class ModelHome {
    private static final ModelHome ourInstance = new ModelHome();

    public static ModelHome getInstance() {
        return ourInstance;
    }

    private ModelHome() {
    }

    private JSONObject objHome;

    public JSONObject getObjHome() {
        return objHome;
    }

    public void setObjHome(JSONObject objHome) {
        this.objHome = objHome;
    }
}
