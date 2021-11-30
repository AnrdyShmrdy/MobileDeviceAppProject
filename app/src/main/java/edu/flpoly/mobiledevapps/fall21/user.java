package edu.flpoly.mobiledevapps.fall21;

public class user {
    //some variables
    String username;
    String phone_number;
    int xp;
    double longitude;
    double latitude;

    //constructor
    public user(String username, String phone_number, int xp){
        this.username = username;
        this.phone_number = phone_number;
        this.xp = xp;
        this.longitude = 0;
        this.latitude = 0;
    }
    //getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
