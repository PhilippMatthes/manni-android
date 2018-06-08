package philippmatthes.com.manni.vvo.Models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Optional;

import philippmatthes.com.manni.vvo.GKCoordinate;
import philippmatthes.com.manni.vvo.WGSCoordinate;

public class StopDeserializer implements JsonDeserializer<Stop> {
    @Override
    public Stop deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String jsonString = json.getAsString();
        String[] components = jsonString.split("\\|");
        if (components.length != 9) {
            throw new JsonParseException("Illegal number of parameters for a Stop.");
        }
        String id = components[0];
        Optional<String> region = components[2].isEmpty() ? Optional.empty() : Optional.of(components[2]);
        String name = components[3];
        try {
            Double x = Double.valueOf(components[5]);
            Double y = Double.valueOf(components[4]);
            Optional<WGSCoordinate> location;
            if (x != 0 && y != 0) {
                location = Optional.of(new WGSCoordinate(x, y));
            } else {
                location = Optional.empty();
            }
            return new Stop(id, name, region, location);
        } catch (NumberFormatException e) {
            throw new JsonParseException("Stop coordinates should be numeric values.");
        }
    }
}
