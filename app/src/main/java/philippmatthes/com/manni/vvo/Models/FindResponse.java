package philippmatthes.com.manni.vvo.Models;

import java.util.Date;

public class FindResponse {

    private final Stop[] stops;
    private final Date expirationTime;

    public FindResponse(Stop[] stops, Date expirationTime) {
        this.stops = stops;
        this.expirationTime = expirationTime;
    }

    public Stop[] getStops() {
        return stops;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }
}
