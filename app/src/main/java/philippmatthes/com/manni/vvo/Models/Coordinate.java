package philippmatthes.com.manni.vvo.Models;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import philippmatthes.com.manni.vvo.GKCoordinate;
import philippmatthes.com.manni.vvo.WGSCoordinate;


public abstract class Coordinate {
    abstract public Optional<GKCoordinate> asGK();
    abstract public Optional<WGSCoordinate> asWGS();
}
