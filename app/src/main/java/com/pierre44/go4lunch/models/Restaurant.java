package com.pierre44.go4lunch.models;

/**
 * Created by pmeignen on 27/10/2021.
 */
public class Restaurant {

    private String placeId;
    private String name;
    private String address;
    private String urlPhoto;
    private int numberOfStars;
    private String OpeningHour;

    // Public Constructor
    public Restaurant(String placeId, String name, String address, String urlPhoto, int numberOfStars, String openingHour) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.urlPhoto = urlPhoto;
        this.numberOfStars = numberOfStars;
        OpeningHour = openingHour;
    }

    //Getters

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public String getOpeningHour() {
        return OpeningHour;
    }


    //Setters

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public void setNumberOfStars(int numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public void setOpeningHour(String openingHour) {
        OpeningHour = openingHour;
    }
}
