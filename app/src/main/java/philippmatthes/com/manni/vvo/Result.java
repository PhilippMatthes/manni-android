package philippmatthes.com.manni.vvo;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import philippmatthes.com.manni.vvo.Models.Response;

@AllArgsConstructor
public class Result<T extends Response> {
    @Getter @Setter private Optional<T> response;
    @Getter @Setter private Optional<DVBError> error;
}
