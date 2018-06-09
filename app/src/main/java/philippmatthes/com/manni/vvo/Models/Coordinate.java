package philippmatthes.com.manni.vvo.Models;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import philippmatthes.com.manni.vvo.GKCoordinate;
import philippmatthes.com.manni.vvo.WGSCoordinate;

@ToString
public abstract class Coordinate {
    abstract public Optional<GKCoordinate> asGK();
    abstract public Optional<WGSCoordinate> asWGS();
}
