package philippmatthes.com.manni.vvo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class RoutesResponse {
    @SerializedName("Routes") @Getter @Setter private List<Route> routes;
    @SerializedName("SessionId") @Getter @Setter private String sessionId;
}
