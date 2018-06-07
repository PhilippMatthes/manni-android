package main.Models;

import java.util.Date;

public class POIResponse {
    private POI[] pins;
    private Date expirationTime;

    public POIResponse(POI[] pins, Date expirationTime) {
        this.pins = pins;
        this.expirationTime = expirationTime;
    }

    public POI[] getPins() {
        return pins;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }
}
