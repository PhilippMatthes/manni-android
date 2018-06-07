package philippmatthes.com.manni.vvo.Models;

public class Diva {
    private String number;
    private String network;

    public Diva(String number, String network) {
        this.number = number;
        this.network = network;
    }

    public String getNetwork() {
        return network;
    }

    public String getNumber() {
        return number;
    }

}
