package philippmatthes.com.manni.vvo;

import java.util.Optional;

public class WGSCoordinate implements Coordinate {

    private Double latitude;
    private Double longitude;

    public WGSCoordinate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public Optional<GKCoordinate> asGK() {
        return GaussKrueger.wgs2gk(this);
    }

    @Override
    public Optional<WGSCoordinate> asWGS() {
        return Optional.of(this);
    }

}
