package com.szabist.fyp.guideus;

/**
 * Created by NerdMacAdmin on 03/01/2018.
 */

public class ModelUserRecycler {
    private String Resturant;
    private String Distance;
    private String Here;

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    private String Longitude;
    private String Latitude;

    public String getoLongitude() {
        return oLongitude;
    }

    public void setoLongitude(String oLongitude) {
        this.oLongitude = oLongitude;
    }

    public String getoLatitude() {
        return oLatitude;
    }

    public void setoLatitude(String oLatitude) {
        this.oLatitude = oLatitude;
    }

    private String oLongitude;
    private String oLatitude;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    private String Url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private String type;
    private String email;
    private String contact;
    private String ID;
    public String getPromoDesc() {
        return PromoDesc;
    }

    public void setPromoDesc(String promoDesc) {
        PromoDesc = promoDesc;
    }

    private String PromoDesc;

    public String getPromoTitle() {
        return PromoTitle;
    }

    public void setPromoTitle(String promoTitle) {
        PromoTitle = promoTitle;
    }

    public String getModelID() {
        return ModelID;
    }

    public void setModelID(String modelID) {
        ModelID = modelID;
    }

    private String PromoTitle;
    private String ModelID;

    public String getResturant() {
        return Resturant;
    }

    public void setResturant(String resturant) {
        Resturant = resturant;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getHere() {
        return Here;
    }

    public void setHere(String here) {
        Here = here;
    }
}
