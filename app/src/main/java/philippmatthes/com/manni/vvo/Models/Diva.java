package philippmatthes.com.manni.vvo.Models;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Diva {
    @SerializedName("Number")@Getter @Setter private String number;
    @SerializedName("Network")@Getter @Setter private String network;
}
