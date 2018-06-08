package philippmatthes.com.manni.vvo.Models;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class RoutesResponse implements Response {
    @SerializedName("Routes") @Getter @Setter private Route[] routes;
    @SerializedName("SessionId") @Getter @Setter private String sessionId;
}
