package com.example.expiryapp.ui.home;

public class items {
    String heading;
    int expiration;

    public int getExpiration() {
        return expiration;
    }

    // TODO: 29/11/2022 make it return actual id from db
    public String getId(){
        return heading;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public items(String heading,int expiration) {
        this.heading = heading;
        this.expiration = expiration;
        //this.id = id
    }

    public String getHeading() {return heading;}

    public void setHeading(String heading) {
        this.heading = heading;
    }

}
