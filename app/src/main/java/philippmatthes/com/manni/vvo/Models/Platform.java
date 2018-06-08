package philippmatthes.com.manni.vvo.Models;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Platform {
    @SerializedName("Name") @Getter @Setter private String name;
    @SerializedName("Type") @Getter @Setter private String type;
}
