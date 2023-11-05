package benicio.soluces.nanacatsitter.model;

public class BannerModel {
    String id, urlBanner;

    public BannerModel() {
    }

    public BannerModel(String id, String urlBanner) {
        this.id = id;
        this.urlBanner = urlBanner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlBanner() {
        return urlBanner;
    }

    public void setUrlBanner(String urlBanner) {
        this.urlBanner = urlBanner;
    }
}
