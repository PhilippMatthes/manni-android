package philippmatthes.com.manni.vvo;

import java.util.Optional;

public class GKCoordinate implements Coordinate {

    private Double x;
    private Double y;

    public GKCoordinate(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }


    @Override
    public Optional<GKCoordinate> asGK() {
        return Optional.of(this);
    }

    @Override
    public Optional<WGSCoordinate> asWGS() {
        return GaussKrueger.gk2wgs(this);
    }

}
