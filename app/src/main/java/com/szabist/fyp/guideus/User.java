package com.szabist.fyp.guideus;

/**
 * Created by alidhanani on 18/11/2017.
 */

public class User {
    public User() {
    }

    public User(String ID, String name, String email, String password, String profileImg, String contact, String address, String area) {
        this.ID = ID;
        Name = name;
        Email = email;
        Password = password;
        ProfileImg = profileImg;
        Contact = contact;
        Address = address;
        Area = area;
    }

    String ID, Name, Email, Password, ProfileImg, Contact, Address, Area, Type, promoShow, promoDesc;

}
