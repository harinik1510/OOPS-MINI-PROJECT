import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import model.Product;
import common.Protocol;

public class WishlistClient extends JFrame {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private JTextArea outputArea;

    public WishlistClient() {
        setTitle("Wishlist App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton addButton = new JButton("Add Product");
        JButton removeButton = new JButton("Remove Product");
        JButton viewButton = new JButton("View Wishlist");

        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(removeButton);
        panel.add(viewButton);
        add(panel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        connectToServer();

        addButton.addActionListener(e -> addProduct());
        removeButton.addActionListener(e -> removeProduct());
        viewButton.addActionListener(e -> viewWishlist());
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            showError("Server not available!");
        }
    }

    private void addProduct() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField priceField = new JTextField();

        Object[] message = {
            "Product ID:", idField,
            "Product Name:", nameField,
            "Model:", modelField,
            "Price:", priceField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Product p = new Product(
                        idField.getText(),
                        nameField.getText(),
                        modelField.getText(),
                        Double.parseDouble(priceField.getText())
                );
                out.writeObject(Protocol.ADD);
                out.writeObject(p);
                out.flush();
                outputArea.setText((String) in.readObject());
            } catch (Exception ex) {
                showError("Error adding product!");
            }
        }
    }

    private void removeProduct() {
        String id = JOptionPane.showInputDialog(this, "Enter Product ID to remove:");
        if (id != null && !id.isEmpty()) {
            try {
                out.writeObject(Protocol.REMOVE);
                out.writeObject(id);
                out.flush();
                outputArea.setText((String) in.readObject());
            } catch (Exception e) {
                showError("Error removing product!");
            }
        }
    }

    private void viewWishlist() {
        try {
            out.writeObject(Protocol.VIEW);
            out.flush();
            outputArea.setText((String) in.readObject());
        } catch (Exception e) {
            showError("Error viewing wishlist!");
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WishlistClient().setVisible(true));
    }
}
