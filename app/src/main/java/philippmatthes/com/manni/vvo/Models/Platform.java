package philippmatthes.com.manni.vvo.Models;

public class Platform {
    private String name;
    private String type;

    public Platform(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
