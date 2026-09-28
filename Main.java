import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();  // Initializing manager
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            try {
                int option = scanner.nextInt();  // Ensuring a valid option, instead of validating the user's input as int
                scanner.nextLine();
                switch (option) {
                    case 1 -> handleAddSeller(manager);  // Add a seller
                    case 2 -> handleAddBuyer(manager);  // Add a buyer
                    case 3 -> handleAddProductForSale(manager);  // Add a product for sale
                    case 4 -> handleShopForProducts(manager);  // Shop for products
                    case 5 -> handleCheckout(manager);  // Checkout
                    case 6 -> handleViewAllBuyers(manager);  // View all buyers
                    case 7 -> handleViewAllSellers(manager);  // View all sellers
                    case 8 -> handlePrintProdsByCategory(manager);  // Print a specific products category
                    case 9 -> handleCreateCartFromOrderHistory(manager);  // Create a cart from order history
                    case 0 -> {
                        System.out.println("Exiting the program.");
                        System.exit(0);  // Exit
                    }
                    default -> System.out.println("Invalid option");  // Any other option is invalid.
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option (a positive integer).");
                scanner.nextLine();  // Clear invalid input from scanner
            }
            System.out.println("Press Enter to return to the main menu.");
            scanner.nextLine();
        }
    }

    public static void printMenu() {  // Prints the main menu
        System.out.println("Main Menu\n=========");
        System.out.println("1. Add a seller");
        System.out.println("2. Add a buyer");
        System.out.println("3. Add a product for sale");
        System.out.println("4. Shop for products");
        System.out.println("5. Checkout");
        System.out.println("6. View all buyers");
        System.out.println("7. View all sellers");
        System.out.println("8. View a specific products category");
        System.out.println("9. Create a shopping cart from order history");
        System.out.println("0. Exit");
    }

    public static void handleAddSeller(Manager manager) {
        String username = validateUsernameDB(manager, "Please enter a seller's username: ");
        String password = validatePassword(manager);

        boolean added = manager.addSellerToDB(username, password);

        if (added) {
            System.out.println("Seller added.");
        }
        else {
            System.out.println("Seller was not added.");
        }
    }

    public static void handleAddBuyer(Manager manager) {
        String username = validateUsernameDB(manager, "Please enter a buyer's username: ");
        String password = validatePassword(manager);
        Address address = requestAddress();

        boolean added = manager.addBuyerToDB(username, password, address);

        if (added) {
            System.out.println("Buyer added.");
        }
        else {
            System.out.println("Buyer was not added.");
        }
    }

    public static void handleAddProductForSale(Manager manager) {
        String sellerUsername = requestExistingSellerUsernameDB(manager);

        Product product = productForSaleInfoDB(sellerUsername, manager);

        boolean added = manager.addProductToDB(sellerUsername, product.getName(), product.getPrice(), product.getCategory(), product.getSpecialboxPrice());

        if (added) {
            System.out.println("Your product has been added for sale.");
        }
        else {
            System.out.println("Product was not added.");
        }
    }

    public static void handleShopForProducts(Manager manager) {
        String buyerUsername = requestExistingBuyerUsernameDB(manager);

        manager.printAllSellersFromDB();

        String sellerUsername = requestExistingSellerUsernameDB(manager);

        while (!manager.sellerHasProductsDB(sellerUsername)) {
            System.out.println("This seller has no products for sale.");
            System.out.print("Please choose a different seller.\n");

            sellerUsername = requestExistingSellerUsernameDB(manager);
        }

        manager.printProductsForSellerDB(sellerUsername);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the name of the product you would like to purchase: ");
        String productName = scanner.nextLine();

        while (manager.getProductIdBySellerAndNameDB(sellerUsername, productName) == -1) {
            System.out.print("Product name invalid. Please enter a product's name from the list above: ");
            productName = scanner.nextLine();
        }

        boolean added = manager.addProductToCartDB(buyerUsername, sellerUsername, productName);

        if (added) {
            System.out.println("Product added to shopping cart.");
        }
        else {
            System.out.println("Product was not added to shopping cart.");
        }
    }

    public static void handleCheckout(Manager manager) {
        Scanner scanner = new Scanner(System.in);

        String buyerUsername = requestExistingBuyerUsernameDB(manager);

        if (manager.isCartEmptyDB(buyerUsername)) {
            System.out.println("Shopping cart empty.");
            return;
        }

        manager.printShoppingCartDB(buyerUsername);

        double totalPrice = manager.calcCartTotalDB(buyerUsername);

        System.out.println("Total price: " + totalPrice + "$\nPress enter for payment");
        scanner.nextLine();

        boolean checkedOut = manager.checkoutDB(buyerUsername, totalPrice);

        if (checkedOut) {
            String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.println("============================");
            System.out.println("Thank you for your purchase!");
            System.out.println("Purchase date: " + formattedDate);
            System.out.println("============================");
        }
        else {
            System.out.println("Checkout failed.");
        }
    }

    public static void handleViewAllBuyers(Manager manager) {
        manager.printAllBuyersFromDB();
    }

    public static void handleViewAllSellers(Manager manager) {
        manager.printAllSellersFromDB();
    }

    public static void handlePrintProdsByCategory(Manager manager) {
        Product.Category category = requestCategory();
        manager.printProductsByCategoryDB(category);
    }

    public static void handleCreateCartFromOrderHistory(Manager manager) {
        Scanner scanner = new Scanner(System.in);

        String buyerUsername = requestExistingBuyerUsernameDB(manager);

        if (!manager.hasOrderHistoryDB(buyerUsername)) {
            System.out.println("No orders history.");
            return;
        }

        if (!manager.isCartEmptyDB(buyerUsername)) {
            System.out.println("! Attention ! Your cart contains items.");
        }

        System.out.print("Would you like to fill your shopping cart with an order from your order history? (yes / no): ");
        String answer = scanner.nextLine();

        while (true) {
            if (answer.equalsIgnoreCase("yes")) {
                break;
            }
            else if (answer.equalsIgnoreCase("no")) {
                return;
            }
            else {
                System.out.print("Invalid option. Please enter a valid option: ");
                answer = scanner.nextLine();
            }
        }

        manager.printOrderHistoryDB(buyerUsername);

        int orderId;

        while (true) {
            try {
                System.out.print("Please choose an order ID from the list above: ");
                orderId = scanner.nextInt();
                scanner.nextLine();

                if (manager.isBuyerOrderDB(buyerUsername, orderId)) {
                    break;
                }
                else {
                    System.out.println("Invalid order ID.");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer number.");
                scanner.nextLine();
            }
        }

        manager.clearShoppingCartDB(buyerUsername);

        boolean copied = manager.copyOrderToCartDB(buyerUsername, orderId);

        if (copied) {
            System.out.println("Your cart has been updated!");
        }
        else {
            System.out.println("Cart was not updated.");
        }
    }

    public static String validateUsernameDB(Manager manager, String prompt) {
    Scanner scanner = new Scanner(System.in);
    String username;

    do {
        System.out.print(prompt);
        username = scanner.nextLine();

        if (!manager.isUsernameValid(username)) {
            System.out.println("Username is invalid. Please enter a valid username.");
            continue;
        }
        else if (manager.isUsernameTakenDB(username)) {
            System.out.println("Username is already taken. Please choose another username.");
            continue;
        }

        break;
    }
    while (true);

    return username;
    }

    public static String validatePassword(Manager manager) {  // Requests a seller/buyer password to register with, returns it if passed validation.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter a password: ");
        String password = scanner.nextLine();
        while (!manager.isPasswordValid(password)) {  // Confirming validation
            System.out.print("Password invalid. Please enter a valid password: ");
            password = scanner.nextLine();
        }
        return password;
    }

    public static Address requestAddress() throws InputMismatchException, IllegalArgumentException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Address details\n===============");
        String stateName = "";
        String cityName = "";
        String streetName = "";
        int buildingNum = 0;

        System.out.print("Please enter your state: ");
        stateName = scanner.nextLine();
        while (stateName.trim().isEmpty()) {
            System.out.print("State name invalid. Please enter a valid state name: ");
            stateName = scanner.nextLine();
        }

        System.out.print("Please enter your city: ");
        cityName = scanner.nextLine();
        while (cityName.trim().isEmpty()) {
            System.out.print("City name invalid. Please enter a valid city name: ");
            cityName = scanner.nextLine();
        }

        System.out.print("Please enter your street name: ");
        streetName = scanner.nextLine();
        while (streetName.trim().isEmpty()) {
            System.out.print("Street name invalid. Please enter a valid street name: ");
            streetName = scanner.nextLine();
        }

        boolean validBuildingNum = false;
        while (!validBuildingNum) {  // Validating building number
            try {
                System.out.print("Please enter your building number: ");
                buildingNum = scanner.nextInt();
                scanner.nextLine();  // Clear the newline character left by nextInt()

                if (buildingNum <= 0) {
                    throw new IllegalArgumentException("Building number must be a positive integer.");
                }
                validBuildingNum = true;  // Building number input is valid
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear the invalid input
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return new Address(stateName, cityName, streetName, buildingNum);
    }

    public static String requestExistingSellerUsernameDB(Manager manager) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter the seller's username: ");
        String sellerUsername = scanner.nextLine();

        while (!manager.isSellerDB(sellerUsername)) {
            System.out.print("Seller does not exist. Please enter a valid seller username: ");
            sellerUsername = scanner.nextLine();
        }

        return sellerUsername;
    }

    public static String requestExistingBuyerUsernameDB(Manager manager) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter the buyer's username: ");
        String buyerUsername = scanner.nextLine();

        while (!manager.isBuyerDB(buyerUsername)) {
            System.out.print("Buyer does not exist. Please enter a valid buyer username: ");
            buyerUsername = scanner.nextLine();
        }

        return buyerUsername;
    }

    public static Product productForSaleInfoDB(String sellerUsername, Manager manager) {
        Scanner scanner = new Scanner(System.in);

        Product.Category category = null;

        while (category == null) {
            System.out.print("Please enter a product category (KIDS, ELECTRICITY, OFFICE, CLOTHING): ");
            String categoryInput = scanner.nextLine().toUpperCase();

            try {
                category = Product.Category.valueOf(categoryInput);
            }
            catch (IllegalArgumentException e) {
                System.out.println("Invalid category entered. Please enter a valid category.");
            }
        }

        System.out.print("Please enter the product's name you would like to add for sale: ");
        String productName = scanner.nextLine();

        while (manager.isProductForSaleDB(sellerUsername, productName) || productName.isEmpty()) {
            if (manager.isProductForSaleDB(sellerUsername, productName)) {
                System.out.print("You are already selling this product. Please enter a different product for sale: ");
            }
            else {
                System.out.print("Invalid product name. Please enter a valid name: ");
            }

            productName = scanner.nextLine();
        }

        double productPrice = getValidPrice(scanner, "Please enter the product's price: ");

        System.out.print("Would you like to sell this product with special packaging? (yes / no): ");
        String specialBoxAnswer = scanner.nextLine();

        double specialBoxPrice = 0;

        while (true) {
            if (specialBoxAnswer.equalsIgnoreCase("yes")) {
                specialBoxPrice = getValidPrice(scanner, "Please enter the price for a special box: ");
                break;
            }
            else if (specialBoxAnswer.equalsIgnoreCase("no")) {
                break;
            }
            else {
                System.out.print("Invalid option. Please enter a valid option: ");
                specialBoxAnswer = scanner.nextLine();
            }
        }

        return new Product(productName, productPrice, 0, category, specialBoxPrice);
    }

    public static Product.Category requestCategory() {  // Returns the desired category from the user
        Scanner scanner = new Scanner(System.in);
        Product.Category category = null;
        while (category == null) {  // Category validation
            System.out.print("Please enter a category to view products from (KIDS, ELECTRICITY, OFFICE, CLOTHING): ");
            String categoryInput = scanner.nextLine().toUpperCase();
            try {
                category = Product.Category.valueOf(categoryInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category entered. Please enter a valid category.");
            }
        }
        return category;
    }

    public static double getValidPrice(Scanner scanner, String prompt) {  // Validates a price given by the user
        double price = 0;
        boolean validPrice = false;
        while (!validPrice) {
            System.out.print(prompt);
            try {
                price = scanner.nextDouble();
                scanner.nextLine();
                if (price < 0) {
                    System.out.println("Invalid price. Price cannot be negative.");
                }
                else {
                    validPrice = true;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.nextLine();
            }
        }
        return price;
    }
}
