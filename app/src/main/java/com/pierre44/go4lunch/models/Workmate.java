package com.pierre44.go4lunch.models;

/**
 * Created by pmeignen on 20/10/2021.
 */
public class Workmate {

    private String iDWorkmate;
    private String nameWorkmate;
    private String photoWorkmate;
    private String emailWorkmate;

    public Workmate(String iDWorkmate, String nameWorkmate, String photoWorkmate, String emailWorkmate) {
        this.iDWorkmate = iDWorkmate;
        this.nameWorkmate = nameWorkmate;
        this.photoWorkmate = photoWorkmate;
        this.emailWorkmate = emailWorkmate;
    }

    public String getIDWorkmate() {
        return iDWorkmate;
    }

    public void setIDWorkmate(String iDWorkmate) {
        this.iDWorkmate = iDWorkmate;
    }

    public String getNameWorkmate() {
        return nameWorkmate;
    }

    public void setNameWorkmate(String nameWorkmate) {
        this.nameWorkmate = nameWorkmate;
    }

    public String getPhotoWorkmate() {
        return photoWorkmate;
    }

    public void setPhotoWorkmate(String photoWorkmate) {
        this.photoWorkmate = photoWorkmate;
    }

    public String getEmailWorkmate() {return emailWorkmate;}

    public void setEmailWorkmate(String emailWorkmate) {
        this.emailWorkmate = emailWorkmate;
    }
}
