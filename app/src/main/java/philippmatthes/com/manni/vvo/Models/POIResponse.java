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
public class POIResponse {
    @SerializedName("Pins") @Getter @Setter private List<POI> pins;
    @SerializedName("ExpirationTime") @Getter @Setter private Date expirationTime;
}
