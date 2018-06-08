package philippmatthes.com.manni.vvo;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import philippmatthes.com.manni.vvo.Models.Coordinate;

@AllArgsConstructor
public class WGSCoordinate extends Coordinate {

    @Getter @Setter private Double latitude;
    @Getter @Setter private Double longitude;

    @Override
    public Optional<GKCoordinate> asGK() {
        return GaussKrueger.wgs2gk(this);
    }

    @Override
    public Optional<WGSCoordinate> asWGS() {
        return Optional.of(this);
    }

}
