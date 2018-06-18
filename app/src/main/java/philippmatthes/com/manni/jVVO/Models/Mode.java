package philippmatthes.com.manni.jVVO.Models;

import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public enum Mode {
    @SerializedName("tram") tram ("tram"),
    @SerializedName("citybus") cityBus ("citybus"),
    @SerializedName("intercitybus") intercityBus ("intercitybus"),
    @SerializedName("suburbanrailway") suburbanRailway ("suburbanrailway"),
    @SerializedName("train") train ("train"),
    @SerializedName("lift") cableway ("lift"),
    @SerializedName("ferry") ferry ("ferry"),
    @SerializedName("alita") hailedSharedTaxi ("alita"),
    @SerializedName("footpath") footpath ("footpath"),
    @SerializedName("rapidtransit") rapidTransit ("rapidtransit"),
    unknown ("unknown");

    private final String rawValue;

    Mode(String s) {
        rawValue = s;
    }

    public String getRawValue() {
        return rawValue;
    }

    public static List<Mode> getAll() {
        Mode[] modes = new Mode[]{
            Mode.tram,
            Mode.cityBus,
            Mode.intercityBus,
            Mode.suburbanRailway,
            Mode.train,
            Mode.cableway,
            Mode.ferry,
            Mode.hailedSharedTaxi
        };
        return Arrays.asList(modes);
    }

    public Optional<URL> getIconURL() {
        if (!Mode.getAll().contains(this)) {
            return Optional.empty();
        }
        String identifier = rawValue;
        if (rawValue.equals("citybus") || rawValue.equals("intercitybus")) {
            identifier = "bus";
        }
        try {
            URL url = new URL("https://www.dvb.de/assets/img/trans-icon/transport-"+identifier+".svg");
            return Optional.of(url);
        } catch (MalformedURLException e) {
            return Optional.empty();
        }

    }
}
