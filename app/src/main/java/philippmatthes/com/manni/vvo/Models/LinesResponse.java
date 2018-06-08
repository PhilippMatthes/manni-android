package philippmatthes.com.manni.vvo.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class LinesResponse {
    @SerializedName("Lines") @Getter @Setter private List<Line> lines;
    @SerializedName("ExpirationTime") @Getter @Setter private String expirationTime;
}
