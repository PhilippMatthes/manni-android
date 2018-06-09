package philippmatthes.com.manni.vvo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class MonitorResponse {
    @Getter @Setter @SerializedName("Name") private String stopName;
    @Getter @Setter @SerializedName("Place") private String place;
    @Getter @Setter @SerializedName("ExpirationTime") private Date expirationTime;
    @Getter @Setter @SerializedName("Departures") private List<Departure> departures;
}
