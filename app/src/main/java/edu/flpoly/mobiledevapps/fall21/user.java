package edu.flpoly.mobiledevapps.fall21;

public class user {
    String username;
    String phone_number;
    int xp;
    double longitude;
    double latitude;

    public user(String username, String phone_number, int xp){
        this.username = username;
        this.phone_number = phone_number;
        this.xp = xp;
        this.longitude = 0;
        this.latitude = 0;
    }


}
