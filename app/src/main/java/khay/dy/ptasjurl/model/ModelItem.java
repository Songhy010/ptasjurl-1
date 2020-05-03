package khay.dy.ptasjurl.model;

public class ModelItem {

    private String thumbnail;
    private String id;
    private String type;
    private String phone1;
    private String price;

    public ModelItem(String thumbnail, String id, String type, String phone1, String price) {
        this.thumbnail = thumbnail;
        this.id = id;
        this.type = type;
        this.phone1 = phone1;
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
