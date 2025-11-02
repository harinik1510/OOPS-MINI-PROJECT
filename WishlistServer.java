import java.io.*;
import java.net.*;
import java.sql.*;
import model.Product;
import common.Protocol;

public class WishlistServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            while (true) {
                String command = (String) in.readObject();

                if (Protocol.ADD.equals(command)) {
                    Product p = (Product) in.readObject();
                    addProductToDB(p);
                    out.writeObject("Product added successfully!");

                } else if (Protocol.REMOVE.equals(command)) {
                    String id = (String) in.readObject();
                    removeProductFromDB(id);
                    out.writeObject("Product removed successfully!");

                } else if (Protocol.VIEW.equals(command)) {
                    out.writeObject(viewWishlist());

                } else if (Protocol.EXIT.equals(command)) {
                    System.out.println("Client disconnected.");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProductToDB(Product p) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO wishlist VALUES (?, ?, ?, ?)")) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getModel());
            ps.setDouble(4, p.getPrice());
            ps.executeUpdate();
        }
    }

    private void removeProductFromDB(String id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM wishlist WHERE id = ?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    private String viewWishlist() throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM wishlist")) {

            while (rs.next()) {
                sb.append(String.format("ID: %s | Name: %s | Model: %s | Price: %.2f\n",
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4)));
            }
        }
        return sb.length() == 0 ? "Wishlist is empty!" : sb.toString();
    }
}
