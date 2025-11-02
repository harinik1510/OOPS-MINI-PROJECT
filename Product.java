import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String model;
    private double price;

    public Product(String id, String name, String model, double price) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getModel() { return model; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Model: %s | Price: %.2f", id, name, model, price);
    }
}
