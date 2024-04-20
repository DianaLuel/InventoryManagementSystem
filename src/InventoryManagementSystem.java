import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class InventoryManagementSystem {
    private static final String PRODUCT_FILE = "products.txt";
    private static final String ADMIN_FILE = "admin.txt";
    private static ArrayList<Product> products = new ArrayList<>();
    private static String adminEmail = "admin@gmail.com";
    private static String adminPassword = "AD@12345";

    public static void main(String[] args) {
        loadProductsFromFile();
        loadAdminCredentials();

        mainMenu();
    }

    private static void loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int code = Integer.parseInt(data[0]);
                String name = data[1];
                float price = Float.parseFloat(data[2]);
                LocalDate expirationDate = LocalDate.parse(data[4]);
                float tax = Float.parseFloat(data[5]);

                Product product = new Product(code, name, price, expirationDate, tax);
                products.add(product);
            }
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    private static void loadAdminCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE))) {
            adminEmail = reader.readLine();
            adminPassword = reader.readLine();
        } catch (IOException e) {
            System.err.println("Error loading admin credentials: " + e.getMessage());
        }
    }

    private static void saveAdminCredentials() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_FILE))) {
            writer.write(adminEmail);
            writer.newLine();
            writer.write(adminPassword);
        } catch (IOException e) {
            System.err.println("Error saving admin credentials: " + e.getMessage());
        }
    }

    private static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n\n\t\t\t______________________\n" +
                    "\t\t\t                      \n" +
                    "\t\t\t    MAIN MENU         \n" +
                    "\t\t\t                      \n" +
                    "\t\t\t______________________\n" +
                    "\t\t\t                      \n" +
                    "\t\t\t 1) Administrator    |\n" +
                    "\t\t\t                     |    \n" +
                    "\t\t\t 2) Buyer            |\n" +
                    "\t\t\t                     |     \n" +
                    "\t\t\t 3) View Inventory   |\n" +
                    "\t\t\t                     |     \n" +
                    "\t\t\t 4) Exit             |\n\n" +
                    "\t\t\t Please select: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    administratorMenu();
                    break;
                case 2:
                    buyerMenu();
                    break;
                case 3:
                    viewInventory();
                    break;
                case 4:
                    saveProductsToFile();
                    saveAdminCredentials();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please select from the given options.");
                    break;
            }
        } while (true);
    }

    private static void administratorMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean loggedIn = false;
        String defaultAdminEmail = "admin123@gmail.com";
        String defaultAdminPassword = "admin123!";

        do {
            if (!loggedIn) {
                System.out.println("Enter admin email: ");
                String emailInput = scanner.nextLine();
                System.out.println("Enter admin password: ");
                String passwordInput = scanner.nextLine();

                if (emailInput.equals(defaultAdminEmail) && passwordInput.equals(defaultAdminPassword)) {
                    loggedIn = true;
                    System.out.println("Login successful!");
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                    continue;
                }
            }

            System.out.println("\n\n\t\t\t______________________\n" +
                    "\t\t\t                      \n" +
                    "\t\t\t Administrator Menu    \n" +
                    "\t\t\t                      \n" +
                    "\t\t\t______________________\n" +
                    "\t\t\t                      \n" +
                    "\t\t\t 1) Add Product        |\n" +
                    "\t\t\t                     |    \n" +
                    "\t\t\t 2) Edit Product       |\n" +
                    "\t\t\t                     |     \n" +
                    "\t\t\t 3) Remove Product     |\n" +
                    "\t\t\t                     |     \n" +
                    "\t\t\t 4) Change Email       |\n" +
                    "\t\t\t                     |     \n" +
                    "\t\t\t 5) Change Password    |\n" +
                    "\t\t\t                     |     \n" +
                    "\t\t\t 6) Back to Main Menu  |\n\n" +
                    "\t\t\t Please select: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    editProduct();
                    break;
                case 3:
                    removeProduct();
                    break;
                case 4:
                    System.out.println("Enter new admin email: ");
                    adminEmail = scanner.nextLine();
                    System.out.println("Email updated successfully.");
                    break;
                case 5:
                    System.out.println("Enter new admin password: ");
                    adminPassword = scanner.nextLine();
                    System.out.println("Password updated successfully.");
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Please select from the given options.");
                    break;
            }
        } while (true);
    }
    private static void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product code: ");
        int code = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Check if the code already exists
        for (Product product : products) {
            if (product.getCode() == code) {
                System.out.println("Product with the given code already exists.");
                return;
            }
        }

        System.out.println("Enter product name: ");
        String name = scanner.nextLine();

        System.out.println("Enter product price: ");
        float price = scanner.nextFloat();

        System.out.println("Enter product expiration date (YYYY-MM-DD): ");
        String dateStr = scanner.next();
        LocalDate expirationDate = LocalDate.parse(dateStr);

        // Calculate tax based on a fixed rate of 0.15 (15%)
        float tax = price * 0.15f; // Assuming tax is 15% of the product price

        Product product = new Product(code, name, price, expirationDate, tax);
        if (products.add(product)) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Failed to add the product.");
        }
    }
    private static void editProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product code to edit: ");
        int code = scanner.nextInt();

        Product productToEdit = findProductByCode(code);
        if (productToEdit != null) {
            System.out.println("Enter new product name (leave empty to skip): ");
            scanner.nextLine(); // Consume newline
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                productToEdit.setName(name);
            }

            System.out.println("Enter new product price (0 to skip): ");
            float price = scanner.nextFloat();
            if (price != 0) {
                productToEdit.setPrice(price);
            }

            System.out.println("Enter new product expiration date (YYYY-MM-DD) (leave empty to skip): ");
            scanner.nextLine(); // Consume newline
            String dateStr = scanner.nextLine();
            if (!dateStr.isEmpty()) {
                LocalDate expirationDate = LocalDate.parse(dateStr);
                productToEdit.setExpirationDate(expirationDate);
            }

            System.out.println("Enter new product tax (0 to skip): ");
            float tax = scanner.nextFloat();
            if (tax != 0) {
                productToEdit.setTax(tax);
            }

            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void removeProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product code to remove: ");
        int code = scanner.nextInt();

        Product productToRemove = findProductByCode(code);
        if (productToRemove != null && products.remove(productToRemove)) {
            System.out.println("Product removed successfully.");
        } else {
            System.out.println("Product not found or could not be removed.");
        }
    }

    private static void buyerMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n\n\t\t\t Buyer Menu");
            System.out.println("\t\t\t 1) Buy Product");
            System.out.println("\t\t\t 2) Go back to Main Menu");
            System.out.println("\t\t\t Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    buyProduct();
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Please select from the given options.");
                    break;
            }
        } while (true);
    }

    private static void buyProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product code: ");
        int code = scanner.nextInt();

        Product product = findProductByCode(code);
        if (product != null) {
            System.out.println("Product found: " + product.toString());
            // Additional logic for purchasing the product can be implemented here
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void viewInventory() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Current Inventory:");
            for (Product product : products) {
                System.out.println(product.toString());
            }
        }
    }

    private static void saveProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product product : products) {
                writer.write(product.toStringForFile());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    private static Product findProductByCode(int code) {
        for (Product product : products) {
            if (product.getCode() == code) {
                return product;
            }
        }
        return null;
    }
}

class Product {
    private int code;
    private String name;
    private float price;
    private LocalDate expirationDate;
    private float tax;
    public Product(int code, String name, float price, LocalDate expirationDate, float tax) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
        this.tax = tax;
    }
    public int getCode() {
        return code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
    public void setTax(float tax) {
        this.tax = tax;
    }

    // Getters and setters for Product attributes

    @Override
    public String toString() {
        return "Code: " + code +
                ", Name: " + name +
                ", Price: $" + price +
                ", Expiry Date: " + expirationDate +
                ", Tax: " + tax;
    }

    public String toStringForFile() {
        return code + "," + name + "," + price + "," + "," + expirationDate + "," + tax;
    }
}