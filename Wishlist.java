import java.util.ArrayList;
import java.util.List;

public class Wishlist {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProductById(String productId) {
        return products.removeIf(p -> p.getId().equals(productId));
    }

    public List<Product> getProducts() {
        return products;
    }

    public void clear() {
        products.clear();
    }

    @Override
    public String toString() {
        if (products.isEmpty()) return "Wishlist is empty!";
        StringBuilder sb = new StringBuilder("Wishlist:\n");
        for (Product p : products) {
            sb.append(p).append("\n");
        }
        return sb.toString();
    }
}
