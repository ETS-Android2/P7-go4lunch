package com.pierre44.go4lunch.models;

/**
 * Created by pmeignen on 20/10/2021.
 */
public class Workmate {

    private String IDWorkmate;
    private String NameWorkmate;
    private String PhotoWorkmate;

    public Workmate(String IDWorkmate, String nameWorkmate, String photoWorkmate) {
        this.IDWorkmate = IDWorkmate;
        NameWorkmate = nameWorkmate;
        PhotoWorkmate = photoWorkmate;
    }

    public String getIDWorkmate() {
        return IDWorkmate;
    }

    public void setIDWorkmate(String IDWorkmate) {
        this.IDWorkmate = IDWorkmate;
    }

    public String getNameWorkmate() {
        return NameWorkmate;
    }

    public void setNameWorkmate(String nameWorkmate) {
        NameWorkmate = nameWorkmate;
    }

    public String getPhotoWorkmate() {
        return PhotoWorkmate;
    }

    public void setPhotoWorkmate(String photoWorkmate) {
        PhotoWorkmate = photoWorkmate;
    }
}
