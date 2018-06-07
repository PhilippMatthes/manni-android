package philippmatthes.com.manni.vvo;

import java.util.Optional;

public interface Coordinate {

    public Optional<GKCoordinate> asGK();
    public Optional<WGSCoordinate> asWGS();

}
