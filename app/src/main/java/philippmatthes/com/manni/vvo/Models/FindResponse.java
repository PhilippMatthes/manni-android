package philippmatthes.com.manni.vvo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class FindResponse implements Response {
    @SerializedName("Points") @Getter @Setter private List<Stop> stops;
    @SerializedName("ExpirationTime") @Getter @Setter private Date expirationTime;
}
