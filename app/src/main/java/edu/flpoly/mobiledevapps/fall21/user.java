package edu.flpoly.mobiledevapps.fall21;

public class user {
    //some variables
    String username;
    String plan;
    int xp;


    //constructor
    public user(String username, String plan, int xp){
        this.username = username;
        this.plan = plan;
        this.xp = xp;
    }
    //getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlan(){return plan;}

    public void setPlan(String plan){this.plan = plan;}

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }


}
