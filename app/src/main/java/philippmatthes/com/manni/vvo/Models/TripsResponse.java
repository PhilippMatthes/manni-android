package philippmatthes.com.manni.vvo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class TripsResponse implements Response {
    @SerializedName("Stops") @Getter @Setter private TripStop[] tripStops;
    @SerializedName("ExpirationTime") @Getter @Setter private Date expirationTime;
}
